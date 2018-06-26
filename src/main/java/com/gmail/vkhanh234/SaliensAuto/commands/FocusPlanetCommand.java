package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.ZoneController;

public class FocusPlanetCommand extends CommandAbstract {

    public FocusPlanetCommand() {
        setName("focusplanet");
        setSyntax("<planet_ID>");
        setDesc("Choose planet to focus when &asearch mode is 2&r. Use &eplanetsinfo&r command to get planets' ID.");
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length==0) return false;
        Main.focusPlanet = args[0];
        ZoneController.focusZone=null;
        Main.debug("Now focus on Planet &e"+args[0]+"&r. Will automatically switch to &asearch mode 1&r if this planet is captured.");
        return true;
    }
}
