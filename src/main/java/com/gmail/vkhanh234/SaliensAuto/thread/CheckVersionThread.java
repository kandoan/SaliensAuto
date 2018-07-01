package com.gmail.vkhanh234.SaliensAuto.thread;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class CheckVersionThread extends Thread {
    @Override
    public void run() {
        while(true) {
            try {
                Main.compareVersion();
            } catch (Exception e) {
            }
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
            }

        }
    }
}