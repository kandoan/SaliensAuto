package data.Planet;

import java.util.List;

public class Planet{
    public String id;
    public PlanetState state;
    public List<Zone> zones;

    public Zone getAvailableZone() {
        int maxDiff = Integer.MIN_VALUE;
        Zone res = null;
        for(Zone zone:zones){
            if(zone.captured || zone.capture_progress>=0.98) continue;
            if(maxDiff<zone.difficulty){
                maxDiff=zone.difficulty;
                res = zone;
            }
        }
        return res;
    }

    public int[] getDifficulties() {
        int[] result = new int[4];
        for(Zone zone:zones){
            if(zone.captured || zone.capture_progress>=0.98) continue;
            result[zone.difficulty]++;
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
}
