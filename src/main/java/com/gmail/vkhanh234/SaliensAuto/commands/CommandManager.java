package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Color;
import com.gmail.vkhanh234.SaliensAuto.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CommandManager {
    LinkedHashMap<String,CommandAbstract> commands = new LinkedHashMap<>();

    public CommandManager() {
        addCommand(new SetTokenCommand());
        addCommand(new SetSearchModeCommand());
        addCommand(new StartCommand());
        addCommand(new StopCommand());
        addCommand(new ExitCommand());
    }

    public void addCommand(CommandAbstract cmd){
        commands.put(cmd.getName(),cmd);
    }

    public void handleCommand(String s){
        String[] spl = s.split(" ");
        if(!commands.containsKey(spl[0])){
            showHelps();
            return;
        }
        CommandAbstract cmd = commands.get(spl[0]);
        if(!cmd.onCommand(buildArgs(spl))){
            cmd.showHelp();
        }
    }

    private String[] buildArgs(String[] spl) {
        String[] args = new String[spl.length-1];
        for(int i=1;i<spl.length;i++) args[i-1]=spl[i];
        return args;
    }

    public void showHelp(CommandAbstract cmd) {
    }
    public void showHelps() {
        Main.debug(Main.highlight("Commands List: ", Color.CYAN_BRIGHT));
        for(CommandAbstract cmd : commands.values()) cmd.showHelp();
        Main.debug("Fill the value in between but don't include the "+Main.highlight("< > and [ ]")+" while using command." );
    }
}
