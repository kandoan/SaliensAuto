package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.PlanetsUtils;
import com.gmail.vkhanh234.SaliensAuto.ProgressUtils;
import com.gmail.vkhanh234.SaliensAuto.colors.Color;
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
        Main.debug("> Planet "+PlanetsUtils.getPlanetsDetailsText(planet));
        Main.debug("\t Zones: "+PlanetsUtils.getZonesText(planet));
        Main.debug("\t Top Clans: "+PlanetsUtils.getTopClanText(planet));
    }
}
