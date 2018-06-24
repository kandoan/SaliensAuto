package com.gmail.vkhanh234.SaliensAuto;

import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planets;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.TopClan;

public class PlanetsUtils {
    public static String getZonesText(Planet p) {
        Planet planetData = Main.getPlanetData(p.id);
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

    public static String getPlanetsDetailsText(Planet planet) {
        return "> Planet id: &e"+planet.id
                +"&r - Name: &e"+planet.extractName()
                +"&r - Captured rate: &a"+ProgressUtils.round(planet.state.capture_progress*100,2)+"%"
                +"&r - Current players: &b"+planet.state.current_players
                +"&r - Total players: &b"+planet.state.total_joins
                +"&r - Priority: &d"+planet.state.priority;
    }

}
