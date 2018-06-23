import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import data.Planet.*;
import data.PlayerInfo.PlayerInfo;
import data.PlayerInfo.PlayerInfoResponse;
import data.ReportScore.ReportScore;
import data.ReportScore.ReportScoreResponse;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public  static String token;
    public static String currentPlanet;
    public static Zone currentZone;
    public static int planetSearchMode = 0;
    public static ProcessThread thread;
    public static boolean pause=true;

    public static boolean noHighDiff=true;
    public static void main(String[] args){
        if(args.length>=1) setToken(args[0]);
        if(args.length>=2) setPlanetSearchMode(Integer.valueOf(args[1]));
        if(args.length>=3) start();

        sendHelp();
        Scanner scanner = new Scanner(System.in);
        while(true){
            String s = scanner.nextLine();
            if(s.length()==0) continue;
            String[] spl = s.split(" ");
            onCommand(spl[0],spl);
        }
    }

    private static void onCommand(String s, String[] args) {
        if(s.equalsIgnoreCase("settoken") && args.length>=2){
            setToken(args[1]);
        }
        else if(s.equalsIgnoreCase("setsearchmode") && args.length>=2){
            setPlanetSearchMode(Integer.parseInt(args[1]));
        }
        else if(s.equalsIgnoreCase("start")){
            start();
        }
        else if(s.equalsIgnoreCase("stop")){
            stop();
        }
        else if(s.equalsIgnoreCase("exit")){
            System.exit(0);
        }
        else{
            sendHelp();
        }
    }

    public static void setPlanetSearchMode(int v) {
        planetSearchMode = v;
        System.out.println("Planet Search Mode has been set to "+planetSearchMode);
    }

    private static void setToken(String s) {
        token = s;
        System.out.println("Token has been set to "+token);
    }

    private static void sendHelp() {
        System.out.println(
                "Commands List: \n"
                +"\t settoken <token> - Set your token. Visit https://steamcommunity.com/saliengame/gettoken to get your token\n"
                +"\t setsearchmode 0 - (Default mode) Choose planet with the highest captured rate to have a chance winning games\n"
                +"\t setsearchmode 1 - Choose planet with the highest difficulties to get more XP\n"
                +"\t start - Start automating\n"
                +"\t stop - Stop automating\n"
                +"\t exit - What can this do? Idk. Figure it out by yourself."
        );
    }

    private static void stop() {
        if(thread!=null && !thread.isInterrupted()) {
            pause=true;
            thread.interrupt();
        }
        System.out.println("Stopping...");
    }

    public static void start(){
        stop();
        pause=false;
        System.out.println("Starting with token "+token+" and search mode "+planetSearchMode+"...");
        thread = new ProcessThread();
        thread.start();

    }

    private static void progress() {
        if(currentPlanet==null) {
            System.out.println("No planet found");
            return;
        }
        else {
            System.out.println("Attempting to progress in planet " + currentPlanet);
            joinPlanet();
        }
        while(!pause) {
            currentZone = getAvailableZone();
            if (currentZone == null) {
                System.out.println("No zone found");
                currentPlanet = getAvailablePlanet();
                return;
            }
            if (!joinZone()) {
                System.out.println("Failed joining zone " + currentZone.zone_position);
                return;
            }
            try {
                System.out.println("Wait 120s");
                Thread.sleep(61000);
                System.out.println("Wait 60s");
                Thread.sleep(30000);
                System.out.println("Wait 30s");
                Thread.sleep(15000);
                System.out.println("Wait 15s");
                Thread.sleep(5000);
                System.out.println("Wait 10s");
                Thread.sleep(5000);
                System.out.println("Wait 5s");
                Thread.sleep(5000);
                if(!reportScore()){
                    System.out.println("Failed completing the instance");
                }
                leaveCurrentGame();
                System.out.println("===================================");
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private static boolean reportScore(){
        int score = getZoneScore();
        System.out.println("Attempting to complete an instance with a score of "+score+" in zone "+currentZone.zone_position+"(difficulty "+currentZone.difficulty+")");
        String data = RequestUtils.post("ITerritoryControlMinigameService/ReportScore","score="+score+"&language=english");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ReportScoreResponse> jsonAdapter = moshi.adapter(ReportScoreResponse.class);
        try {
            ReportScoreResponse res = jsonAdapter.fromJson(data);
            if(res!=null && res.response!=null){
                ReportScore response = res.response;
                if(response==null || response.new_score==null) return false;
                System.out.println("Completed an instance. You have reached level "+response.new_level+" ("+response.new_score+"/"+response.next_level_score+")");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int getZoneScore() {
        int score=0;
        switch (currentZone.difficulty){
            case 1: score=5; break;
            case 2: score=10; break;
            case 3: score=20; break;
        }
        return score*120;
    }

    private static boolean joinZone() {
        System.out.println("Attempting to join zone "+currentZone.zone_position);
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
        RequestUtils.post("ITerritoryControlMinigameService/JoinPlanet","id="+currentPlanet);
    }


    public static void leaveCurrentGame(){
        PlayerInfo info = getPlayerInfo();
        if(info.active_zone_game!=null){
            RequestUtils.post("IMiniGameService/LeaveGame","gameid="+info.active_zone_game);
            System.out.println("Left game "+info.active_zone_game);
        }
        if(info.active_planet!=null) currentPlanet = info.active_planet;
    }

    public static void leaveCurrentPlanet(){
        PlayerInfo info = getPlayerInfo();
        if(info.active_planet!=null){
            RequestUtils.post("IMiniGameService/LeaveGame","gameid="+info.active_planet);
            System.out.println("Left planet "+info.active_planet);
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
        System.out.println("Searching for planet...");
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
            System.out.println("- Planet "+planet.id+"("+planet.state.name+")'s priority is "+planet.state.priority);
            if(min>planet.state.priority){
                min = planet.state.priority;
                id=planet.id;
            }
        }
        System.out.println("=> Choose planet "+id);
        return id;
    }

    public static String getMostXpPlanet(Planets planets){
        noHighDiff=true;
        int[] max = new int[4];
        String id = "1";
        for(Planet planet:planets.planets){
            Planet planetData = getPlanetData(planet.id);
            int[] difficuties = planetData.getDifficulties();
            System.out.println("- Planet "+planet.id+"("+planet.state.name+") has "+difficuties[1]+" low, "+difficuties[2]+" medium and "+difficuties[3]+" high");
            if(difficuties[3]>0) noHighDiff=false;
            for(int i=3;i>=1;i--){
                if(max[i]<difficuties[i]){
                    max=difficuties;
                    id=planet.id;
                    break;
                }
                else if(max[i]>difficuties[i]) break;
            }
        }
        System.out.println("=> Choose planet "+id);
        return id;
    }

    public static Zone getAvailableZone(){
        Planet planet = getPlanetData(currentPlanet);
        if(planet==null) return null;
        Zone zone = planet.getAvailableZone();
        if(planetSearchMode==1 && zone.difficulty<3 && !noHighDiff)
            return null;
        else return zone;
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

    private static class ProcessThread extends Thread {
        @Override
        public void run() {
            while(!pause) {
                try {
                    leaveCurrentGame();
                    leaveCurrentPlanet();
                    progress();
                }catch (Exception e){}
            }
            System.out.println("Automation stopped");
        }
    }
}
