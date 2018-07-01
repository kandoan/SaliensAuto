package com.gmail.vkhanh234.SaliensAuto;

import com.gmail.vkhanh234.SaliensAuto.colors.Color;
import com.gmail.vkhanh234.SaliensAuto.colors.ColorParser;
import com.gmail.vkhanh234.SaliensAuto.commands.CommandManager;
import com.gmail.vkhanh234.SaliensAuto.data.Boss.BossPlayer;
import com.gmail.vkhanh234.SaliensAuto.data.Boss.BossStatus;
import com.gmail.vkhanh234.SaliensAuto.data.Boss.ReportBossDamage;
import com.gmail.vkhanh234.SaliensAuto.data.Boss.ReportBossDamageResponse;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planets;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.PlanetsResponse;
import com.gmail.vkhanh234.SaliensAuto.data.PlayerInfo.PlayerInfo;
import com.gmail.vkhanh234.SaliensAuto.data.PlayerInfo.PlayerInfoResponse;
import com.gmail.vkhanh234.SaliensAuto.data.ReportScore.ReportScore;
import com.gmail.vkhanh234.SaliensAuto.data.ReportScore.ReportScoreResponse;
import com.gmail.vkhanh234.SaliensAuto.data.RequestResult;
import com.gmail.vkhanh234.SaliensAuto.searchmode.SearchMode;
import com.gmail.vkhanh234.SaliensAuto.searchmode.SearchModeManager;
import com.gmail.vkhanh234.SaliensAuto.thread.CheckVersionThread;
import com.gmail.vkhanh234.SaliensAuto.thread.ProcessThread;
import com.gmail.vkhanh234.SaliensAuto.thread.SearchThread;
import com.gmail.vkhanh234.SaliensAuto.utils.ProgressUtils;
import com.gmail.vkhanh234.SaliensAuto.utils.RequestUtils;
import com.gmail.vkhanh234.SaliensAuto.utils.TextUtils;
import com.gmail.vkhanh234.SaliensAuto.utils.VersionUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;


public class Main {
    public static final double MAX_CAPTURE_RATE = 0.95;

    public  static String token;
    public static String currentPlanet;
    public static String nextPlanet;
    public static int planetSearchMode = 1;
    public static int accountId=0;

    public static CheckVersionThread versionThread;
    public static ProcessThread thread;
    public static SearchThread searchThread;

    public static boolean pause=true;
    public static boolean instantRestart=false;
    public static int vcCounter=5;
    public static int noHighCounter=0;
    public static int maxDiff=0;
    public static int[] totalDiff = new int[5];

    public static int searchCounter=0;
    public static boolean stealthSearch=false;

    public static String focusPlanet;

    public static CommandManager commandManager = new CommandManager();
    public static SearchModeManager searchModeManager = new SearchModeManager();

    public static boolean disableUpdate = false;


    public static void main(String[] args){
        AnsiConsole.systemInstall();

        checkVersion();

        debug(highlight("SaliensAuto "+ VersionUtils.getLocalVersion(),Color.LIGHT_PURPLE));
        debug("&cPlease keep checking &ahttps://github.com/KickVN/SaliensAuto &cregularly in case there is a new update");
        debug("&cAnd please close all runninng instances of Saliens on the same account in this program. For exampe, close your Saliens on web browser.");
        commandManager.showHelps();

        if(args.length>=1) setToken(args[0]);
        if(args.length>=2) setPlanetSearchMode(Integer.valueOf(args[1]));
        if(args.length>=3 && !args[2].equals("0")) start();
        if(args.length>=4) setAccountId(Long.valueOf(args[3]));

        Scanner scanner = new Scanner(System.in);
        while(true){
            String s = scanner.nextLine();
            if(s.length()==0) continue;
            try {
                commandManager.handleCommand(s);
            }catch (Exception e){if(!(e instanceof NullPointerException)) e.printStackTrace();}
        }
    }

    public static void setPlanetSearchMode(int v) {
        planetSearchMode = v;
        debug(highlight("Planet Search Mode")+" has been set to "+highlight(planetSearchMode+""));
    }

    public static void setToken(String s) {
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
        debug("Starting >> Token: "+highlight(token)+" - Search mode: "+highlight(planetSearchMode+""));
        if(planetSearchMode==2) {
            if(focusPlanet==null){
                debug("\t &cPlease set a focused planet with &efocusplanet &ccommand first");
                return;
            }
            debug("\t Focused on planet &e"+focusPlanet+" &r"+(ZoneController.focusZone!=null?("and zone &e"+(Integer.valueOf(ZoneController.focusZone)+1)):""));
        }
        startProcessThread();

    }

    public static void startProcessThread() {
        stop();
        pause=false;
        thread = new ProcessThread();
        thread.start();
    }

    public static String highlight(String s) {
        return highlight(s,Color.YELLOW);
    }
    public static String highlight(String s, Color color) {
        return color.getTag()+s+Color.RESET.getTag();
    }

    public static void progress() {
        ZoneController.clearCachedProgress();
        nextPlanet = Main.getAvailablePlanet();
        if(nextPlanet==null) {
            debug(highlight("No planet found",Color.RED));
            return;
        }
        else {
            ZoneController.clearSkipZones();
            currentPlanet=nextPlanet;
            joinPlanet();
//            if(!joinPlanet()) return;
        }
        Main.debug("Searching for zone");
        ZoneController.currentZone = ZoneController.loadBestZone(currentPlanet);
        ZoneController.nextZone=ZoneController.currentZone;
        while(!pause) {
            stopSearchThread();
            if(!currentPlanet.equals(nextPlanet)){
                ZoneController.clearSkipZones();
                leaveCurrentPlanet();
                currentPlanet=nextPlanet;
                joinPlanet();
//                if(!joinPlanet()) return;
            }
            ZoneController.currentZone = ZoneController.nextZone;
            if (ZoneController.currentZone == null) {
                debug(highlight("No zone found",Color.RED));
                return;
            }
            if(ZoneController.currentZone.boss_active){
                progressBoss();
                instantRestart=true;
                return;
            }
            else {
                if (!ZoneController.joinZone(ZoneController.currentZone)) {
                    debug(highlight("Failed to join zone " + highlight(ZoneController.currentZone.getZoneText() + ""), Color.RED));
                    return;
                }
                try {
                    debug("&dWait 110s to complete the instance");
                    searchWhileWaiting();
                    Thread.sleep(50000);
                    debug("&dWait 60s");
                    Thread.sleep(30000);
                    debug("&dWait 30s");
                    Thread.sleep(15000);
                    debug("&dWait 15s");
                    Thread.sleep(5000);
                    debug("&dWait 10s");
                    Thread.sleep(5000);
                    debug("&dWait 5s");
                    Thread.sleep(5000);
                    if (!reportScore()) {
                        debug(highlight("Failed to complete the instance. It could mean the zone is captured. Or you're opening Saliens somewhere else. Please close all things related to Saliens.", Color.RED));
                    }
                    leaveCurrentGame();
                    debug(highlight("===================================", Color.GREEN));
                } catch (InterruptedException e) {
                    if (!pause) e.printStackTrace();
                    return;
                }
            }
        }
    }

    private static void progressBoss() {
        ZoneController.joinZone(ZoneController.currentZone,true);
        int attemp=0;
        long healTime = randomNumber(26,28);
        while (true){
            if(attemp>=10) return;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
            int damage = 1;
            int damageTaken = 0;
            int heal=0;
            if(healTime--<=0){
                heal=1;
                debug("Attempt to use &eHeal&r skill");
//                damageTaken=randomNumber(20,80);
                healTime=26;
            }

            ReportBossDamageResponse res = reportBossDamage(damage,heal,damageTaken);
            if(res==null){
                debug("&cNo boss data found");
                return;
            }
            if(res.eResult==11){
                debug("&cYou probably died. Attempt to restart...");
                return;
            }
            if(res.response!=null){
                ReportBossDamage response = res.response;
                if(response.boss_status!=null) {
                    BossStatus status = response.boss_status;
                    if(status.boss_players!=null && status.boss_players.size()>0) {
                        if(Main.accountId>0) {
                            BossPlayer main=null;
                            for (BossPlayer player : status.boss_players) {
                                if (player.accountid != Main.accountId) continue;
                                main=player;
                                break;
                            }
                            if(main!=null) {
                                debug("Your HP: &e" + TextUtils.formatNumber(main.hp) + "&r/&e" + TextUtils.formatNumber(main.max_hp) + "&r - XP earned: &b" + TextUtils.formatNumber(main.xp_earned));
                                if(main.hp<=0){
                                    debug("\t&bNo HP left. Attempt to restart...");
                                    return;
                                }
                            }
                        }
                    }
                    if(status.game_over){
                        debug("&bBoss is done!");
                        return;
                    }
                    if(status.waiting_for_players){
                        debug("&aWaiting for players...");
                        continue;
                    }
                    debug("Boss HP: &e"+TextUtils.formatNumber(status.boss_hp)+"&r/&e"+TextUtils.formatNumber(status.boss_max_hp)+"&r - Laser used: &e"+response.num_laser_uses+"&r - Team heals: &e"+response.num_team_heals);
                    if(status.boss_max_hp<=0){
                        debug("&eBoss died");
                        return;
                    }
                }
                else{
                    debug("&aWaiting...");
                    attemp++;
                }
            }
        }
    }

    private static ReportBossDamageResponse reportBossDamage(int damage, int heal, int damageTaken) {
        RequestResult result = RequestUtils.post("ITerritoryControlMinigameService/ReportBossDamage","use_heal_ability="+heal+"&damage_to_boss="+damage
                +"&damage_taken="+damageTaken);
        String data = result.data;
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ReportBossDamageResponse> jsonAdapter = moshi.adapter(ReportBossDamageResponse.class);
        try {
            ReportBossDamageResponse res = jsonAdapter.fromJson(data);
            res.eResult=result.eResult;
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int randomNumber(int min,int max){
        return new Random().nextInt((max - min)+1) + min;
    }

    private static boolean reportScore(){
        int score = ZoneController.getZoneScore();
        debug("Finishing an instance >> Score: &e"+score
                +"&r - Zone "+TextUtils.getZoneDetailsText(ZoneController.currentZone));
        String data = RequestUtils.post("ITerritoryControlMinigameService/ReportScore","score="+score+"&language=english").data;
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ReportScoreResponse> jsonAdapter = moshi.adapter(ReportScoreResponse.class);
        try {
            ReportScoreResponse res = jsonAdapter.fromJson(data);
            if(res!=null && res.response!=null){
                ReportScore response = res.response;
                if(response==null || response.new_score==null) return false;
                debug("&bFinished. Your progress >> "+TextUtils.getPlayerProgress(response));
                int scoreLeft = Integer.valueOf(response.next_level_score)-Integer.valueOf(response.new_score);
                debug("\t&bApprox time left to reach Level &e"+(response.new_level+1)+"&b: &d"+ProgressUtils.getTimeLeft(scoreLeft,ZoneController.getPointPerSec(ZoneController.currentZone.difficulty)));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void changeGroup(String clanid){
        debug("Changing group...");
        PlayerInfo info = getPlayerInfo();
        if(info == null || info.clan_info==null || !String.valueOf(info.clan_info.accountid).equals(clanid)) {
            RequestUtils.post("ITerritoryControlMinigameService/RepresentClan", "clanid=" + clanid);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            PlayerInfo check = getPlayerInfo();
            if(check.clan_info!=null && String.valueOf(check.clan_info.accountid).equals(clanid)) {
                debug("Successfully changed group to &e"+check.clan_info.name);
            } else debug("&cError:&r Couldn't change group. Make sure the groupid is correct and you have joined the group on Steam.");
        }
        else debug("&cError:&r You have already represented this group");
    }


    private static boolean joinPlanet() {
        debug("Attempt to progress in planet " + highlight(currentPlanet));
        RequestResult result = RequestUtils.post("ITerritoryControlMinigameService/JoinPlanet","id="+currentPlanet);
        if(result.eResult!=1){
            debug("Can't join Planet &e"+currentPlanet);
            return false;
        }
        return true;
    }


    public static boolean leaveCurrentGame(){
        debug("Attempt to leave previous zone");
        PlayerInfo info = getPlayerInfo();
        if(info.active_zone_game!=null){
            RequestResult result = RequestUtils.post("IMiniGameService/LeaveGame","gameid="+info.active_zone_game);
            if(result.eResult==1) {
                debug(highlight("Left game " + highlight(info.active_zone_game), Color.AQUA));
                return true;
            }
            else debug("&eError:&r Can't leave zone");
        }
        if(info.active_planet!=null) currentPlanet = info.active_planet;
        return false;
    }

    public static boolean leaveCurrentPlanet(){
        debug("Attempt to leave previous planet");
        PlayerInfo info = getPlayerInfo();
        if(info!=null && info.active_planet!=null){
            RequestResult result = RequestUtils.post("IMiniGameService/LeaveGame","gameid="+info.active_planet);
            if(result.eResult==1) {
                debug(highlight("Left planet " + highlight(info.active_planet), Color.AQUA));
                return true;
            }
            else debug("&eError:&r Can't leave planet");
        }
        return false;
    }

    public static PlayerInfo getPlayerInfo(){
        String dat = RequestUtils.post("ITerritoryControlMinigameService/GetPlayerInfo","").data;
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
        String res = getSearchModeInstance(planetSearchMode).search();
        Main.debug("&a=> Choose planet &e"+res);
        return res;
    }


    public static boolean isNoHighDiff() {
        return totalDiff[3]<=0 && totalDiff[4]<=0;
    }

    public static boolean isOnlyEasyDiff() {
        return isNoHighDiff() && totalDiff[2]<=0 && totalDiff[1]>0;
    }

    public static Planet getPlanetData(String id){
        String data = RequestUtils.get("GetPlanet","id="+id).data;
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
        String res = RequestUtils.get( "GetPlanets", "active_only=1" ).data;
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

    public static void searchWhileWaiting(){
        stopSearchThread();
        searchThread = new SearchThread();
        searchThread.run();
    }

    private static void stopSearchThread() {
        if(searchThread!=null && !searchThread.isInterrupted()){
            searchThread.interrupt();
        }
    }

    public static void checkVersion(){
//        if(disableUpdate) return;
//        //Only check every 5 zones
//        if(vcCounter<5){
//            vcCounter++;
//            return;
//        }
//        vcCounter=0;
//
//        if(versionThread!=null && !versionThread.isInterrupted()) versionThread.interrupt();
        versionThread = new CheckVersionThread();
        versionThread.start();
    }

    public static SearchMode getSearchModeInstance(int mode){
        return searchModeManager.getSearchMode(mode);
    }
    public static SearchMode getSearchMode(){
        return getSearchModeInstance(planetSearchMode);
    }

    public static void debug(String s){
        String msg = "["+new SimpleDateFormat("HH:mm:ss").format(new Date())+"] "+s+"&r";
        System.out.println(ColorParser.parse(msg));
    }

    public static void setAccountId(long v) {
        accountId = Long.valueOf(v&0xFFFFFFFF).intValue();
        Main.debug("&eAccount ID &rhas been set to &e"+accountId);
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

}
