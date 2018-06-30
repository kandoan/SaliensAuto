package com.gmail.vkhanh234.SaliensAuto.thread;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class CheckVersionThread extends Thread {
    @Override
    public void run() {
        while(true) {
            try {
                Main.compareVersion();
                Thread.sleep(600000);
            } catch (Exception e) {
                if (!(e instanceof NullPointerException))
                    e.printStackTrace();
            }
        }
    }
}