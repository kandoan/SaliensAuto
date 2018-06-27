package com.gmail.vkhanh234.SaliensAuto.thread;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.ZoneController;
import com.gmail.vkhanh234.SaliensAuto.searchmode.HighestCapturedMode;
import com.gmail.vkhanh234.SaliensAuto.utils.TextUtils;

public class SearchThread extends Thread {
        @Override
        public void run() {
            try {
                if (Main.planetSearchMode == 0 || Main.planetSearchMode==1) {
                    if (Main.searchCounter >= 4) {
                        Main.searchCounter = 0;
                        Main.debug("&bChecking planets' progress while waiting");
                        Main.nextPlanet = Main.getSearchMode().search();
                    } else Main.searchCounter++;
                }
                Main.debug("Searching for next zone while waiting");
                ZoneController.nextZone = ZoneController.loadBestZone(Main.currentPlanet);
                boolean isNullZone = ZoneController.nextZone == null;
                if (isNullZone) {
                    Main.debug("&cError: &rNo zone found. Searching for planet instead...");
                    Main.nextPlanet = Main.getSearchMode().search();
                    if (Main.planetSearchMode == 0 || Main.planetSearchMode==1) Main.searchCounter = 0;
                }
                if(Main.nextPlanet!=null && !Main.nextPlanet.equals(Main.currentPlanet)) {
//                    if(!isNullZone) ZoneController.clear();
                    ZoneController.clear();
                    ZoneController.nextZone = ZoneController.loadBestZone(Main.nextPlanet);
                    Main.debug("Next planet is Planet &e" + Main.nextPlanet);
                }
                if(ZoneController.nextZone!=null) {
                    Main.debug("Next zone is Zone " + TextUtils.getZoneDetailsText(ZoneController.nextZone));
                    if(Main.currentPlanet.equals(Main.nextPlanet) && ZoneController.nextZone.zone_position==ZoneController.currentZone.zone_position){
                        ZoneController.currentZone=ZoneController.nextZone;
                    }
                }
            } catch (Exception e){
                if(!(e instanceof NullPointerException))
                    e.printStackTrace();
            }
        }
    }