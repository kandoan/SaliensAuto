package com.gmail.vkhanh234.SaliensAuto.data.Planet;


import com.gmail.vkhanh234.SaliensAuto.Main;

public class Zone {
    public int zone_position,type,difficulty;
    public boolean captured;
    public double capture_progress;
    public String gameid;

    public String getDifficultyText(){
        if(type==4) return Main.addDiffColor(Main.getDiffText(4),4);
        return Main.addDiffColor(Main.getDiffText(difficulty),difficulty);
    }
}
