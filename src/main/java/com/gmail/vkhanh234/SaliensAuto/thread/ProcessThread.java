package com.gmail.vkhanh234.SaliensAuto.thread;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class ProcessThread extends Thread {
        @Override
        public void run() {
            while(!Main.pause) {
                try {
                    Main.leaveCurrentGame();
                    Main.leaveCurrentPlanet();
                    Main.progress();
                }catch (Exception e){
                    if(!(e instanceof NullPointerException))
                        e.printStackTrace();
                }
                if(!Main.instantRestart) {
                    Main.debug("&cRestarting in 8s...");
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    Main.instantRestart = false;
                    Main.debug("&cRestarting...");
                }
            }
            Main.debug("&cAutomation stopped");
        }
    }