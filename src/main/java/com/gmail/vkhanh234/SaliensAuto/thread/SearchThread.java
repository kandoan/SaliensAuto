package com.gmail.vkhanh234.SaliensAuto.thread;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.ZoneController;
import com.gmail.vkhanh234.SaliensAuto.searchmode.HighestCapturedMode;
import com.gmail.vkhanh234.SaliensAuto.utils.TextUtils;

public class SearchThread extends Thread {
        @Override
        public void run() {
            try {
                if (Main.planetSearchMode == 0) {
                    if (HighestCapturedMode.counter >= 4) {
                        HighestCapturedMode.counter = 0;
                        Main.debug("&bChecking planets' progress while waiting");
                        Main.nextPlanet = Main.getSearchMode().search();
                    } else HighestCapturedMode.counter++;
                }
                Main.debug("Searching for next zone while waiting");
                ZoneController.nextZone = ZoneController.loadBestZone(Main.currentPlanet);
                if (ZoneController.nextZone == null) {
                    Main.debug("&cError: &rNo zone found. Searching for planet instead...");
                    Main.nextPlanet = Main.getSearchMode().search();
                    ZoneController.nextZone = ZoneController.loadBestZone(Main.nextPlanet);
                    if (Main.planetSearchMode == 0) HighestCapturedMode.counter = 0;
                }
                if(Main.nextPlanet!=null && !Main.nextPlanet.equals(Main.currentPlanet)) Main.debug("Next planet is Planet &e"+Main.nextPlanet);
                if(ZoneController.nextZone!=null) {
                    Main.debug("Next zone is Zone " + TextUtils.getZoneDetailsText(ZoneController.nextZone));
                    if(ZoneController.nextZone.zone_position==ZoneController.currentZone.zone_position){
                        ZoneController.currentZone=ZoneController.nextZone;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }