package com.gmail.vkhanh234.SaliensAuto.thread;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.ZoneController;
import com.gmail.vkhanh234.SaliensAuto.utils.TextUtils;

public class SearchThread extends Thread {
        @Override
        public void run() {
            try {
                if (Main.searchCounter >= 4) {
                    Main.searchCounter = 0;
                    Main.debug("&bChecking planets' progress while waiting");
                    Main.stealthSearch=false;
                    Main.nextPlanet = Main.getSearchMode().search();
                } else {
                    Main.searchCounter++;
                    Main.stealthSearch=true;
                    Main.nextPlanet = Main.getSearchMode().search();
                    Main.stealthSearch=false;
                }
                if(Main.nextPlanet!=null && !Main.nextPlanet.equals(Main.currentPlanet)) {
                    ZoneController.clearCachedProgress();
                    Main.debug("Next planet is Planet &e" + Main.nextPlanet);
                }
                Main.debug("Searching for next zone while waiting");
                ZoneController.nextZone = ZoneController.loadBestZone(Main.nextPlanet);
                if(ZoneController.nextZone!=null) {
                    Main.debug("Next zone is Zone " + TextUtils.getZoneDetailsText(ZoneController.nextZone));
                    if(ZoneController.nextZone.boss_active){
                        Main.debug("&cBOSS ALERT!! RESTARTING TO FIGHT BOSS...");
                        Main.startProcessThread();
                        return;
                    }
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