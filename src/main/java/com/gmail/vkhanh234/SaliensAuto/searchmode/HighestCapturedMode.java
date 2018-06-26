package com.gmail.vkhanh234.SaliensAuto.searchmode;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planets;
import com.gmail.vkhanh234.SaliensAuto.utils.TextUtils;

public class HighestCapturedMode extends SearchMode {
    protected String search(Planets planets){
        String result = null;
        double max = 0;
        for(Planet planet:planets.planets){
            if(planet.state==null || !planet.state.active || planet.state.captured) continue;
            Main.debug("> Planet "+TextUtils.getPlanetsDetailsText(planet));
            if(max<planet.state.capture_progress){
                max = planet.state.priority;
                result = planet.id;
            }
        }
        return result;
    }
}
