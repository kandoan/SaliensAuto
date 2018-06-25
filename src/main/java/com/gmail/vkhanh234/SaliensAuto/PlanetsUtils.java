package com.gmail.vkhanh234.SaliensAuto;

import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planets;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.TopClan;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Zone;

public class PlanetsUtils {
    public static String getZonesText(Planet p) {
        Planet planetData;
        if(p.zones==null || p.zones.size()==0) planetData = Main.getPlanetData(p.id);
        else planetData=p;
        int[] difficuties = planetData.getDifficulties();
        String s = "";
        for(int i=1;i<=4;i++){
            s+=Main.addDiffColor(difficuties[i]+" "+Main.getDiffText(i),i)+", ";
        }
        return s.substring(0,s.length()-2);
    }

    public static String getTopClanText(Planet planet) {
        String s="";
        for(TopClan clan:planet.top_clans){
            s+="&b"+clan.clan_info.name+" &r(&a"+clan.num_zones_controled+" "+(clan.num_zones_controled>1?"zones":"zone")+"&r), ";
        }
        return s.substring(0,s.length()-2);
    }

    public static String getZoneDetailsText(Zone zone){
        return "Position: &e"+(zone.zone_position+1)+" ("+getRowColumnText(zone.zone_position)+")"
                +"&r - Difficulty: "+zone.getDifficultyText()
                +"&r - Captured rate: &a"+ProgressUtils.round(zone.capture_progress*100,2)+"%"
                ;
    }

    private static String getRowColumnText(int pos) {
        int col = pos%12;
        int row = pos/12;
        return "Row "+(row+1)+", Column "+(col+1);
    }

    public static String getPlanetsDetailsText(Planet planet) {
        return "ID: &e"+planet.id
                +"&r - Name: &e"+planet.extractName()
                +"&r - Captured rate: &a"+ProgressUtils.round(planet.state.capture_progress*100,2)+"%"
                +"&r - Current players: &b"+planet.state.current_players
                +"&r - Total players: &b"+planet.state.total_joins
                +"&r - Priority: &d"+planet.state.priority;
    }

    public static String getZonePosition(String s) {
        if(s.contains(",")){
            String[] spl = s.split(",");
            int row = Integer.valueOf(spl[0])-1;
            int col = Integer.valueOf(spl[1])-1;
            return String.valueOf(row*12+col);
        }
        return String.valueOf(Integer.valueOf(s)-1);
    }
}
