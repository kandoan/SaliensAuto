package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class StopCommand extends CommandAbstract {

    public StopCommand() {
        setName("stop");
        setDesc("Stop the automating process");
    }

    @Override
    public boolean onCommand(String[] args) {
        Main.stop();
        return true;
    }
}
