package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.PlanetsUtils;

public class FocusZoneCommand extends CommandAbstract {

    public FocusZoneCommand() {
        setName("focuszone");
        setSyntax("<zone_position>");
        setDesc("(Optional) Choose a zone to focused on when &asearch mode is 2&r.\n" +
                "\t\t\t&r position can be a number start from 1. For example: &e60&r means 60th zone when counting left to right, top to bottom\n" +
                "\t\t\t&r or can be <row>,<column>. For example: &e3,5&r means zone in row 3 and column 5.");
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length==0) return false;
        Main.focusZone = PlanetsUtils.getZonePosition(args[0]);
        Main.debug("Now focus on Zone &e"+args[0]+"&r. Will automatically search for hardest zone if this zone is captured.");
        return true;
    }
}
