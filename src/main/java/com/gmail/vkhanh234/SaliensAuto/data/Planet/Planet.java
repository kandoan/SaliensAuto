package com.gmail.vkhanh234.SaliensAuto.data.Planet;

import com.gmail.vkhanh234.SaliensAuto.data.PlayerInfo.ClanInfo;

import java.util.List;

public class Planet{
    public String id;
    public PlanetState state;
    public List<Zone> zones;
    public List<TopClan> top_clans;

    public Zone getAvailableZone() {
        int maxDiff = Integer.MIN_VALUE;
        Zone res = null;
        for(Zone zone:zones){
            if(zone.captured || zone.capture_progress>=0.93) continue;
            int diff = zone.difficulty;
            if(zone.type==4) diff=4;
            if(maxDiff<diff){
                maxDiff=diff;
                res = zone;
            }
        }
        return res;
    }

    public int[] getDifficulties() {
        int[] result = new int[5];
        for(Zone zone:zones){
            if(zone.captured || zone.capture_progress>=0.93) continue;
            if(zone.type==4) result[4]++;
            else result[zone.difficulty]++;
        }
        return result;
    }

    public int getDiffValue(int diff){
        switch (diff){
            case 1: return 1;
            case 2: return 100;
            case 3: return 10000;
        }
        return 1;
    }

    public String extractName() {
        String name = state.name;
        String t = name.replace("#TerritoryControl_Planet","");
        if(t.equals(name)) return name;
        String[] spl = t.split("_");
        String res = "";
        for(String s:spl) res+=s+" ";
        return res.trim();
    }
}
