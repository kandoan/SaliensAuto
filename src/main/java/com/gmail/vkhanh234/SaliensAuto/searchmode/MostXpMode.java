package com.gmail.vkhanh234.SaliensAuto.searchmode;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.ZoneController;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planet;
import com.gmail.vkhanh234.SaliensAuto.data.Planet.Planets;
import com.gmail.vkhanh234.SaliensAuto.utils.TextUtils;

public class MostXpMode extends SearchMode {
    protected String search(Planets planets){
        Main.totalDiff = new int[5];
        Main.maxDiff = 0;
        int[] max = new int[5];
        String result=null;
        for(Planet planet:planets.planets){
            Planet planetData = Main.getPlanetData(planet.id);
            int[] difficuties = ZoneController.getDifficulties(planetData);
            Main.debug("> Planet "+TextUtils.getPlanetsDetailsText(planetData));
            Main.debug("\tZones: "+TextUtils.getZonesText(planetData));
            for(int i=4;i>=1;i--){
                Main.totalDiff[i]+=difficuties[i];
                if(difficuties[i]>0 && i>Main.maxDiff) Main.maxDiff=i;
                if(max[i]<difficuties[i]){
                    max=difficuties;
                    result=planet.id;
                    break;
                }
                else if(max[i]>difficuties[i]) break;
            }
        }
        if(Main.isOnlyEasyDiff()){
            Main.debug("&aThere are only "+Main.addDiffColor("easy zones",1)+" left. Start searching for highest captured planets.");
            return Main.getSearchModeInstance(0).search(planets);
        }
        return result;
    }
}
