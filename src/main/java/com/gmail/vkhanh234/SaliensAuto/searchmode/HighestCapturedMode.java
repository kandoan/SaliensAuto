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
            if(planet.state==null || !planet.state.active || planet.state.captured || planet.state.capture_progress>= Main.MAX_CAPTURE_RATE) continue;
            if(!Main.stealthSearch) Main.debug("> Planet "+TextUtils.getPlanetsDetailsText(planet));
            if(max<planet.state.capture_progress){
                max = planet.state.capture_progress;
                result = planet.id;
            }
        }
        return result;
    }
}
