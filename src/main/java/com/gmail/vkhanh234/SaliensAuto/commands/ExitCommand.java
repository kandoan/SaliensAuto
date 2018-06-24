package com.gmail.vkhanh234.SaliensAuto.commands;

public class ExitCommand extends CommandAbstract {

    public ExitCommand() {
        setName("exit");
        setDesc("Exit the program");
    }

    @Override
    public boolean onCommand(String[] args) {
        System.exit(0);
        return true;
    }
}
