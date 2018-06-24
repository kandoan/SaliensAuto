package com.gmail.vkhanh234.SaliensAuto.commands;

import com.gmail.vkhanh234.SaliensAuto.Main;

public abstract class CommandAbstract {
    String name,desc,syntax ;

    public abstract boolean onCommand(String[] args);


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public boolean hasSynxtax() {
        return syntax!=null && syntax.length()>0;
    }

    public void showHelp() {
        Main.debug("\t "+Main.highlight(getName()+(hasSynxtax()?" "+getSyntax():""))+" - "+getDesc()+"");
    }
}
