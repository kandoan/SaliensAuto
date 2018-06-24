package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class StartCommand extends CommandAbstract {

    public StartCommand() {
        setName("start");
        setDesc("Start the automating process");
    }

    @Override
    public boolean onCommand(String[] args) {
        Main.start();
        return true;
    }
}
