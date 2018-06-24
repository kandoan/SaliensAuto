package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class SetTokenCommand extends CommandAbstract {

    public SetTokenCommand() {
        setName("settoken");
        setSyntax("<token>");
        setDesc("Set your token. Visit https://steamcommunity.com/saliengame/gettoken to get it.");
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length==0) return false;
        Main.token = args[0];
        return true;
    }
}
