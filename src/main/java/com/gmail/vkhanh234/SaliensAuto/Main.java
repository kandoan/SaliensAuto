package com.gmail.vkhanh234.SaliensAuto;

import com.gmail.vkhanh234.SaliensAuto.colors.Color;
import com.gmail.vkhanh234.SaliensAuto.colors.ColorParser;
import com.gmail.vkhanh234.SaliensAuto.commands.CommandManager;
import com.gmail.vkhanh234.SaliensAuto.data.PlayerInfo.PlayerInfo;
import com.gmail.vkhanh234.SaliensAuto.data.PlayerInfo.PlayerInfoResponse;
import com.gmail.vkhanh234.SaliensAuto.data.ReportScore.ReportScore;
import com.gmail.vkhanh234.SaliensAuto.data.ReportScore.ReportScoreResponse;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.fusesource.jansi.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class Main {
    public  static String token;
    public static String currentPlanet;
    public static Zone currentZone;
    public static int planetSearchMode = 0;

    public static CheckVersionThread versionThread;
    public static ProcessThread thread;

    public static boolean pause=true;
    public static boolean noHighDiff=true;

    public static int vcCounter=5;
    public static int noHighCounter=0;

    public static CommandManager commandManager = new CommandManager();
    public static long lastSuccess=System.currentTimeMillis();
    public static void main(String[] args){
        AnsiConsole.systemInstall();

        checkVersion();

        debug(highlight("SaliensAuto "+ VersionUtils.getLocalVersion(),Color.LIGHT_PURPLE));
        debug("&rPlease keep checking &ahttps://github.com/KickVN/SaliensAuto &cregularly in case there is a new update");
        commandManager.showHelps();

        if(args.length>=1) setToken(args[0]);
        if(args.length>=2) setPlanetSearchMode(Integer.valueOf(args[1]));
        if(args.length>=3) start();

        Scanner scanner = new Scanner(System.in);
        while(true){
            String s = scanner.nextLine();
            if(s.length()==0) continue;
            try {
                commandManager.handleCommand(s);
            }catch (Exception e){e.printStackTrace();}
        }
    }

    public static void setPlanetSearchMode(int v) {
        planetSearchMode = v;
        debug(highlight("Planet Search Mode")+" has been set to "+highlight(planetSearchMode+""));
    }

    private static void setToken(String s) {
        token = s;
        debug(highlight("Token")+" has been set to "+highlight(token));
    }

    public static void stop() {
        if(thread!=null && !thread.isInterrupted()) {
            pause=true;
            thread.interrupt();
        }
        debug(highlight("Stopping...",Color.RED));
    }

    public static void start(){
        stop();
        pause=false;
        debug("Starting >> Token: "+highlight(token)+" - Search mode: "+highlight(planetSearchMode+""));
        thread = new ProcessThread();
        thread.start();

    }

    public static String highlight(String s) {
        return highlight(s,Color.YELLOW);
    }
    public static String highlight(String s, Color color) {
        return color.getTag()+s+Color.RESET.getTag();
    }

    private static void progress() {
        if(currentPlanet==null) {
            debug(highlight("No planet found",Color.RED));
            return;
        }
        else {
            joinPlanet();
        }
        while(!pause) {
            currentZone = getAvailableZone();
            if (currentZone == null) {
                debug(highlight("No zone found",Color.RED));
                return;
            }
            if (!joinZone()) {
                debug(highlight("Failed joining zone " + highlight(currentZone.zone_position+""),Color.RED));
                return;
            }
            try {
                debug("Wait 110s");
                checkVersion();
                Thread.sleep(50000);
                debug("Wait 60s");
                Thread.sleep(30000);
                debug("Wait 30s");
                Thread.sleep(15000);
                debug("Wait 15s");
                Thread.sleep(5000);
                debug("Wait 10s");
                Thread.sleep(5000);
                debug("Wait 5s");
                Thread.sleep(5000);
                if(!reportScore()){
                    debug(highlight("Failed to complete the instance. It could mean the zone is captured.",Color.RED));
                }
                leaveCurrentGame();
                debug(highlight("===================================",Color.GREEN));
            } catch (InterruptedException e) {
                if(!pause) e.printStackTrace();
                return;
            }
        }
    }

    private static boolean reportScore(){
        int score = getZoneScore();
        debug("Finishing an instance >> Score: &e"+score
                +"&r - Zone "+PlanetsUtils.getZoneDetailsText(currentZone));
        String data = RequestUtils.post("ITerritoryControlMinigameService/ReportScore","score="+score+"&language=english");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ReportScoreResponse> jsonAdapter = moshi.adapter(ReportScoreResponse.class);
        try {
            ReportScoreResponse res = jsonAdapter.fromJson(data);
            if(res!=null && res.response!=null){
                ReportScore response = res.response;
                if(response==null || response.new_score==null) return false;
                debug("&bFinished. Your progress >> Level: &e"+response.new_level
                        +"&b - XP: &r(&e"+response.new_score+"&r/&e"+response.next_level_score+"&r)"
                        +"&b - XP Percent: &a"+ProgressUtils.getPercent(Integer.valueOf(response.new_score),Integer.valueOf(response.next_level_score))+"%"
                        +"&b - XP Required: &e"+(Integer.valueOf(response.next_level_score)-Integer.valueOf(response.new_score)));
                int scoreLeft = Integer.valueOf(response.next_level_score)-Integer.valueOf(response.new_score);
                debug("\t&bApprox time left to reach Level &e"+(response.new_level+1)+"&b: &d"+ProgressUtils.getTimeLeft(scoreLeft,getPointPerSec(currentZone.difficulty)));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int getZoneScore() {
        int score=getPointPerSec(currentZone.difficulty);
        return score*120;
    }

    private static int getPointPerSec(int difficulty) {
        switch (currentZone.difficulty){
            case 1: return 5;
            case 2: return 10;
            case 3: return 20;
        }
        return 0;
    }

    public static void changeGroup(String clanid){
        debug("Changing group...");
        PlayerInfo info = getPlayerInfo();
        if(info.clan_info==null || !String.valueOf(info.clan_info.accountid).equals(clanid)) {
            RequestUtils.post("ITerritoryControlMinigameService/RepresentClan", "clanid=" + clanid);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            PlayerInfo check = getPlayerInfo();
            if(check.clan_info!=null && String.valueOf(check.clan_info.accountid).equals(clanid)) {
                debug("Successfully changed group to &e"+check.clan_info.name);
            } else debug("&aError:&r Can't changed group. Make sure the groupid is correct.");
        }
        else debug("&aError:&r You have already represented this group");
    }

    private static boolean joinZone() {
//        debug("Joining zone "+currentZone.zone_position+" (difficulty "+highlight(currentZone.difficulty+"")+")");
        debug("Joining Zone "+PlanetsUtils.getZoneDetailsText(currentZone));
        String data = RequestUtils.post("ITerritoryControlMinigameService/JoinZone","zone_position="+currentZone.zone_position);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ZoneInfoResponse> jsonAdapter = moshi.adapter(ZoneInfoResponse.class);
        try {
            ZoneInfoResponse response = jsonAdapter.fromJson(data);
            if(response==null || response.response==null || response.response.zone_info==null || response.response.zone_info.captured) return false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void joinPlanet() {
        debug("Attempt to progress in planet " + highlight(currentPlanet));
        RequestUtils.post("ITerritoryControlMinigameService/JoinPlanet","id="+currentPlanet);
    }


    public static void leaveCurrentGame(){
        debug("Attempt to leave previous zone");
        PlayerInfo info = getPlayerInfo();
        if(info.active_zone_game!=null){
            RequestUtils.post("IMiniGameService/LeaveGame","gameid="+info.active_zone_game);
            debug(highlight("Left game "+highlight(info.active_zone_game),Color.AQUA));
        }
        if(info.active_planet!=null) currentPlanet = info.active_planet;
    }

    public static void leaveCurrentPlanet(){
        debug("Attempt to leave previous planet");
        PlayerInfo info = getPlayerInfo();
        if(info.active_planet!=null){
            RequestUtils.post("IMiniGameService/LeaveGame","gameid="+info.active_planet);
            debug(highlight("Left planet "+highlight(info.active_planet),Color.AQUA));
        }
        currentPlanet = getAvailablePlanet();
    }

    public static PlayerInfo getPlayerInfo(){
        String dat = RequestUtils.post("ITerritoryControlMinigameService/GetPlayerInfo","");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<PlayerInfoResponse> jsonAdapter = moshi.adapter(PlayerInfoResponse.class);
        try {
            PlayerInfoResponse response = jsonAdapter.fromJson(dat);
            if(response==null || response.response==null) return null;
            return response.response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAvailablePlanet()
    {
        debug(highlight("Searching for planet...",Color.AQUA));
        Planets planets = getPlanets();
        if(planets==null) return null;
        if(planetSearchMode==0) return getTopPriorityPlanet(planets);
        else return getMostXpPlanet(planets);
    }
    public static String getTopPriorityPlanet(Planets planets){
        int min = Integer.MAX_VALUE;
        String id="1";
        for(Planet planet:planets.planets){
            if(planet.state==null || !planet.state.active || planet.state.captured) continue;
            debug("> Planet "+PlanetsUtils.getPlanetsDetailsText(planet));
            if(min>planet.state.priority){
                min = planet.state.priority;
                id=planet.id;
            }
        }
        debug(highlight("=> Choose planet "+highlight(id),Color.GREEN));
        return id;
    }

    public static String getMostXpPlanet(Planets planets){
        noHighDiff=true;
        int[] max = new int[5];
        String id = "1";
        for(Planet planet:planets.planets){
            Planet planetData = getPlanetData(planet.id);
            int[] difficuties = planetData.getDifficulties();
            debug("> Planet "+PlanetsUtils.getPlanetsDetailsText(planetData));
            debug("\tZones: "+PlanetsUtils.getZonesText(planetData));
            if(difficuties[3]>0 || difficuties[4]>0) noHighDiff=false;
            for(int i=4;i>=1;i--){
                if(max[i]<difficuties[i]){
                    max=difficuties;
                    id=planet.id;
                    break;
                }
                else if(max[i]>difficuties[i]) break;
            }
        }
        debug(highlight("=> Choose planet "+highlight(id),Color.GREEN));
        return id;
    }

    public static Zone getAvailableZone(){
        debug("Searching for zone");
        Planet planet = getPlanetData(currentPlanet);
        if(planet==null) return null;
        Zone zone = planet.getAvailableZone();
        if(planetSearchMode==1 && zone.difficulty<3 && (!noHighDiff || noHighCounter>=4)) {
            noHighCounter=0;
            return null;
        }
        else {
            if(noHighDiff) noHighCounter++;
            return zone;
        }
    }

    public static Planet getPlanetData(String id){
        String data = RequestUtils.get("GetPlanet","id="+id);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<PlanetsResponse> jsonAdapter = moshi.adapter(PlanetsResponse.class);
        try {
            PlanetsResponse response = jsonAdapter.fromJson(data);
            if(response==null || response.response==null || response.response.planets==null || response.response.planets.size()==0) return null;
            return response.response.planets.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Planets getPlanets(){
        String res = RequestUtils.get( "GetPlanets", "active_only=1" );
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<PlanetsResponse> jsonAdapter = moshi.adapter(PlanetsResponse.class);
        try {
            PlanetsResponse planets = jsonAdapter.fromJson(res);
            if(planets==null || planets.response==null || planets.response.planets==null || planets.response.planets.size()==0) return null;
            return planets.response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void changeClan(String id){
//        PlayerInfo info = getPlayerInfo();
//        if(info.clan_info==null || !String.valueOf(info.clan_info.accountid).equalsIgnoreCase(id)){
//
//        }
//    }

    public static void compareVersion(){
        String remoteVer = VersionUtils.getRemoteVersion();
        String localVer = VersionUtils.getLocalVersion();
        if(remoteVer.equalsIgnoreCase(localVer)) return;
        debug(highlight("=================================",Color.RED));
        debug(highlight("There is a new version available: ",Color.GREEN)+highlight("SaliensAuto "+remoteVer));
        debug(highlight("Your current version: ",Color.GREEN)+highlight("SaliensAuto "+localVer));
        debug(highlight("Go here and download latest version: ",Color.GREEN)+highlight("https://github.com/KickVN/SaliensAuto/releases",Color.AQUA));
        debug(highlight("=================================",Color.RED));
    }

    public static String getDiffText(int diff){
        switch (diff) {
            case 1: return "easy";
            case 2: return "medium";
            case 3: return "hard";
            case 4: return "BOSS";
        }
        return "???";
    }

    public static String addDiffColor(String s,int diff){
        return highlight(s,getDiffColor(diff));
    }

    public static Color getDiffColor(int diff){
        switch (diff) {
            case 1: return Color.GREEN;
            case 2: return Color.AQUA;
            case 3: return Color.LIGHT_PURPLE;
            case 4: return Color.RED;
        }
        return Color.RESET;
    }

    public static void checkVersion(){
        //Only check every 5 zones
        if(vcCounter<5){
            vcCounter++;
            return;
        }
        vcCounter=0;

        if(versionThread!=null && !versionThread.isInterrupted()) versionThread.interrupt();
        versionThread = new CheckVersionThread();
        versionThread.start();
    }

    public static void debug(String s){
        String msg = "["+new SimpleDateFormat("HH:mm:ss").format(new Date())+"] "+s+"&r";
        System.out.println(ColorParser.parse(msg));
//        log(msg);
    }

//    private static void log(String msg) {
//        BufferedWriter out = null;
//        try
//        {
//            FileWriter fstream = new FileWriter("SaliensAuto logs.txt", true);
//            out = new BufferedWriter(fstream);
//            out.write(+"\n");
//
//        }
//        catch (IOException e)
//        {
////            e.printStackTrace();
//        }
//        finally
//        {
//            if(out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
////                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private static class ProcessThread extends Thread {
        @Override
        public void run() {
            while(!pause) {
                try {
                    leaveCurrentGame();
                    leaveCurrentPlanet();
                    progress();
                }catch (Exception e){e.printStackTrace();}
                debug("Restarting in 5s...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }
            debug(highlight("Automation stopped",Color.RED));
        }
    }
    private static class CheckVersionThread extends Thread {
        @Override
        public void run() {
            try {
                compareVersion();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
