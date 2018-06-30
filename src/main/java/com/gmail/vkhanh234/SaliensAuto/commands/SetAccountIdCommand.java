package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class SetAccountIdCommand extends CommandAbstract {

    public SetAccountIdCommand() {
        setName("setaccountid");
        setSyntax("<id>");
        setDesc("Set your Account ID. You can pass SteamID into this too.");
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length==0) return false;
        try {
            Main.setAccountId(Long.valueOf(args[0]));
        }catch (Exception e){}
        return true;
    }
}
