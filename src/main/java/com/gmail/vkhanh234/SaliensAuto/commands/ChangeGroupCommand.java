package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class ChangeGroupCommand extends CommandAbstract {

    public ChangeGroupCommand() {
        setName("changegroup");
        setSyntax("<groupid>");
        setDesc("Change the group you represent. ID &a33035916&r is &a/r/saliens&r group.");
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length==0) return false;
        Main.changeGroup(args[0]);
        return true;
    }
}
