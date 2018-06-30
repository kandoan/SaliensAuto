package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class SetAccountIdCommand extends CommandAbstract {

    public SetAccountIdCommand() {
        setName("setaccountid");
        setSyntax("<accountid>");
        setDesc("Set your Account ID.");
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length==0) return false;
        try {
            Main.setAccountId(Integer.valueOf(args[0]));
        }catch (Exception e){}
        return true;
    }
}
