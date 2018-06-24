package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.ProgressUtils;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planets;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.TopClan;
import com.gmail.vkhanh234.SaliensAuto.data.PlayerInfo.ClanInfo;

public class PlanetsInfoCommand extends CommandAbstract {

    public PlanetsInfoCommand() {
        setName("planetsinfo");
        setDesc("Show brief info of all active planets");
    }

    @Override
    public boolean onCommand(String[] args) {
        Planets planets = Main.getPlanets();
        for(Planet planet:planets.planets){
            showInfo(planet);
        }

        return true;
    }

    private void showInfo(Planet planet) {
        Main.debug(
                "> Planet id: &e"+planet.id
                +"&r - Name: &e"+planet.extractName()
                +"&r - Captured rate: &a"+ProgressUtils.round(planet.state.capture_progress*100,2)+"%"
                +"&r - Current players: &b"+planet.state.current_players
                +"&r - Total players: &b"+planet.state.total_joins
                +"&r - Priority: &d"+planet.state.priority
        );
        Main.debug("\t Top Clans: "+getTopClanText(planet));
    }

    private String getTopClanText(Planet planet) {
        String s="";
        for(TopClan clan:planet.top_clans){
            s+="&b"+clan.clan_info.name+" &r(&a"+clan.num_zones_controled+" zones&r), ";
        }
        return s.substring(0,s.length()-2);
    }
}
