package com.gmail.vkhanh234.SaliensAuto;

import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Zone;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.ZoneInfoResponse;
import com.gmail.vkhanh234.SaliensAuto.utils.ProgressUtils;
import com.gmail.vkhanh234.SaliensAuto.utils.RequestUtils;
import com.gmail.vkhanh234.SaliensAuto.utils.TextUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ZoneController {

    public static String focusZone;
    public static Zone currentZone;
    public static Zone nextZone;
    public static boolean switchRecently=true;

    public static LinkedList<Double> cachedProgress = new LinkedList<>();

    public static List<Integer> skipZones = new ArrayList<>();


    public static boolean joinZone(Zone zone, boolean boss) {
        Main.debug("Joining Zone "+TextUtils.getZoneDetailsText(zone.predict())+" - Planet &e"+Main.currentPlanet);
        String data;
        if(boss) data = RequestUtils.post("ITerritoryControlMinigameService/JoinBossZone","zone_position="+zone.zone_position);
        else data = RequestUtils.post("ITerritoryControlMinigameService/JoinZone","zone_position="+zone.zone_position);
        boolean result=false;
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ZoneInfoResponse> jsonAdapter = moshi.adapter(ZoneInfoResponse.class);
        try {
            ZoneInfoResponse response = jsonAdapter.fromJson(data);
            if(response==null || response.response==null || response.response.zone_info==null || response.response.zone_info.captured) result=false;
            else result=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!result) {
            skipZones.add(zone.zone_position);
            Main.debug("\tSkip zone &e"+zone.zone_position+"&r from now on until switching to other planet.");
            Main.debug("\t&bPlease wait for the program to restart itself and everything will work again.");
        }
        return result;
    }
    public static boolean joinZone(Zone zone) {
        return joinZone(zone,false);
    }

    public static Zone loadBestZone(String p){
        Zone zone;
        if(cachedProgress.size()>0){
            double predict = getPredictProgress();
            Main.debug("\t Finding zone with captured rate lower than &e"+ProgressUtils.round((0.985-predict*2)*100,2)+"%");
            zone = findBestZone(p,0.985-predict*2);
        }
        else zone = findBestZone(p,0.9);
        if(currentZone!=null && zone!=null && zone.zone_position==currentZone.zone_position){
            if(!switchRecently) cacheProgress(zone.capture_progress-currentZone.capture_progress);
            else switchRecently=false;
        }
        else cachedProgress.clear();
        return zone;
    }

    public static Zone findBestZone(String p, double maxProgress){
        Planet planet = Main.getPlanetData(p);
        if(planet==null) return null;
        return findBestZone(planet,maxProgress);
    }

    private static Zone findBestZone(Planet planet, double maxProgress) {
        int maxDiff = Integer.MIN_VALUE;
        Zone res = null;
        for(Zone zone:planet.zones){
            if(zone.captured || zone.capture_progress>=maxProgress) continue;
            if(!zone.boss_active && zone.capture_progress<0.4 && skipZones.contains(zone.zone_position)) continue;
            if(Main.planetSearchMode==2 && focusZone!=null && String.valueOf(zone.zone_position).equals(focusZone)) return zone;
            int diff = zone.difficulty;
            if(zone.boss_active) diff=4;
            if(maxDiff<diff){
                maxDiff=diff;
                res = zone;
            }
        }
        if(Main.planetSearchMode==2 && focusZone!=null){
            Main.debug("Focused Zone &e"+(Integer.valueOf(focusZone)+1)+" &r has been captured. Now search for hardest zone instead.");
            focusZone = null;
        }
        return res;
    }

    public static int[] getDifficulties(Planet planet) {
        int[] result = new int[5];
        for(Zone zone:planet.zones){
            if(zone.captured || zone.capture_progress>=Main.MAX_CAPTURE_RATE) continue;
            if(!zone.boss_active && zone.capture_progress<0.4 && skipZones.contains(zone.zone_position)) continue;
            if(zone.boss_active) result[4]++;
            else result[zone.difficulty]++;
        }
        return result;
    }

    public static int getZoneScore() {
        int score=getPointPerSec(currentZone.difficulty);
        return score*120;
    }

    public static int getPointPerSec(int difficulty) {
        switch (currentZone.difficulty){
            case 1: return 5;
            case 2: return 10;
            case 3: return 20;
        }
        return 0;
    }

    public static double getMaxProgress(){
        double max=0;
        for(Double d:cachedProgress){
            if(max<d) max=d;
        }
        return max;
    }

    public static double getAverageProgress() {
        if(cachedProgress.size()==0) return 0;
        double sum = 0;
        for(Double d:cachedProgress) sum+=d;
        return sum/cachedProgress.size();
    }
    private static double getPredictProgress() {
        return (getAverageProgress()+getMaxProgress())/2;
    }

    public static void clearCachedProgress() {
        switchRecently=true;
        cachedProgress.clear();
    }

    public static void clearSkipZones(){
        if(Main.currentPlanet!=null && Main.nextPlanet!=null && !Main.currentPlanet.equals(Main.nextPlanet)) skipZones.clear();
    }
    private static void cacheProgress(double v) {
        cachedProgress.add(v);
    }
}
