package com.gmail.vkhanh234.SaliensAuto.searchmode;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planets;

public class FocusMode extends SearchMode {
    public String search(){
        int min = Integer.MAX_VALUE;
        Planet data = Main.getPlanetData(Main.focusPlanet);
        if(!data.state.active || data.state.captured || data.state.capture_progress>Main.MAX_CAPTURE_RATE){
            Main.debug("&bFocused Planet &e"+Main.focusPlanet+" &bhas been captured");
            Main.debug("&bAtuomatically switched to search mode &e1 &binstead");
            Main.planetSearchMode=1;
            return Main.getAvailablePlanet();
        }
        return Main.focusPlanet;
    }
}
