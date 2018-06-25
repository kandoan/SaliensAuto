package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public class SetSearchModeCommand extends CommandAbstract {

    public SetSearchModeCommand() {
        setName("setsearchmode");
        setSyntax("<0/1/2>");
        setDesc("Set the search mode."
                +"\n\t\t\tSet to 0 (default) to search for highest captured rate planet."
                +"\n\t\t\tSet to 1 to search for planet with most XP reward."
                +"\n\t\t\tSet to 2 to only choose focused planet."
        );
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length==0) return false;
        try{
            int mode = Integer.valueOf(args[0]);
            Main.planetSearchMode = mode;
            Main.debug("&eSearch mode &rhas been set to &e"+Main.planetSearchMode);
        }
        catch (Exception e){}
        return true;
    }
}
