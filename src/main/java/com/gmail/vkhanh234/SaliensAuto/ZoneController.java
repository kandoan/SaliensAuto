package com.gmail.vkhanh234.SaliensAuto;

import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Zone;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.ZoneInfoResponse;
import com.gmail.vkhanh234.SaliensAuto.searchmode.MostXpMode;
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

    public static LinkedList<Double> cachedProgress = new LinkedList<>();


    public static boolean joinZone(Zone zone) {
        Main.debug("Joining Zone "+TextUtils.getZoneDetailsText(zone));
        String data = RequestUtils.post("ITerritoryControlMinigameService/JoinZone","zone_position="+zone.zone_position);
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

    public static void loadBestZone(){
//        Zone zone = findBestZone(0.98);
//        if(zone.zone_position==currentZone.zone_position){
//            cachedProgress.add(zone.capture_progress);
//            if(cachedProgress.size()>2){
//                double average = getAverageProgress();
//                double lastProgress = getLastProgress();
//                if(average+lastProgress>0.98){
//                    zone = findBestZone(lastProgress);
//                }
//            }
//        }
//        else{
//            cachedProgress.clear();
//            cachedProgress.add(zone.capture_progress);
//        }
//        currentZone=zone;
        Main.debug("Searching for zone");
        Zone zone;
        if(cachedProgress.size()>0){
            double average = getAverageProgress();
            zone = findBestZone(0.99-average);
//            Main.debug("\t Average progress: &e+"+ProgressUtils.round(average*100,2)+"&r. Find zone with max progress: &e"+ProgressUtils.round((0.9999-average)*100,2));
        }
        else zone = findBestZone(Main.MAX_CAPTURE_RATE);
        if(currentZone!=null && zone.zone_position==currentZone.zone_position){
            cacheProgress(zone.capture_progress-currentZone.capture_progress);
        }
        else cachedProgress.clear();
        currentZone = zone;
    }

    public static Zone findBestZone(double maxProgress){
        Planet planet = Main.getPlanetData(Main.currentPlanet);
        if(planet==null) return null;
        Zone zone = findBestZone(planet,maxProgress);
        if(Main.planetSearchMode==1 && ((zone.difficulty>1 && zone.difficulty<Main.maxDiff) || Main.noHighCounter>=4)) {
            Main.noHighCounter=0;
            Main.instantRestart=true;
            return null;
        }
        else {
            if(Main.planetSearchMode==1 && (zone.difficulty<Main.maxDiff || zone.difficulty==1)) Main.noHighCounter++;
            return zone;
        }
    }

    private static Zone findBestZone(Planet planet, double maxProgress) {
        int maxDiff = Integer.MIN_VALUE;
        Zone res = null;
        for(Zone zone:planet.zones){
            if(zone.captured || zone.capture_progress>=maxProgress) continue;
            if(Main.planetSearchMode==2 && focusZone!=null && String.valueOf(zone.zone_position).equals(focusZone)) return zone;
            int diff = zone.difficulty;
            if(zone.type==4) diff=4;
            if(maxDiff<diff){
                maxDiff=diff;
                res = zone;
            }
        }
        if(Main.planetSearchMode==2 && focusZone!=null){
            Main.debug("Focused Zone &e"+(focusZone+1)+" &r has been captured. Now search for hardest zone instead.");
            focusZone = null;
        }
        return res;
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

    private static double getAverageProgress() {
        double sum = 0;
        for(Double d:cachedProgress) sum+=d;
        return sum/cachedProgress.size();
    }

    public static void clear() {
        cachedProgress.clear();
    }
    private static void cacheProgress(double v) {
        cachedProgress.add(v);
    }
}
