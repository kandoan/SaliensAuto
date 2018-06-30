package com.gmail.vkhanh234.SaliensAuto.data.Planet;


import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.ZoneController;

public class Zone {
    public int zone_position,type,difficulty;
    public boolean captured,boss_active;
    public double capture_progress;
    public String gameid;

    public String getDifficultyText(){
        if(type==4) return Main.addDiffColor(Main.getDiffText(4),4);
        return Main.addDiffColor(Main.getDiffText(difficulty),difficulty);
    }

    public String getZoneText(){
        return String.valueOf(zone_position+1);
    }

    public Zone predict() {
        Zone z = new Zone();
        z.zone_position=this.zone_position;
        z.type=this.type;
        z.difficulty=this.difficulty;
        z.boss_active=this.boss_active;
        z.captured=this.captured;
        z.gameid=this.gameid;
        z.capture_progress=this.capture_progress+ZoneController.getAverageProgress();
        return z;
    }
}
