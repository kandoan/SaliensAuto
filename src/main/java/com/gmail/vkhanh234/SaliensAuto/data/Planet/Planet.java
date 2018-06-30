package com.gmail.vkhanh234.SaliensAuto.data.Planet;

import com.gmail.vkhanh234.SaliensAuto.Main;
import com.gmail.vkhanh234.SaliensAuto.data.PlayerInfo.ClanInfo;

import java.util.List;

public class Planet{
    public String id;
    public PlanetState state;
    public List<Zone> zones;
    public List<TopClan> top_clans;

    public String extractName() {
        String name = state.name;
        String t = name.replace("#TerritoryControl_Planet","");
        if(t.equals(name)) return name;
        String[] spl = t.split("_");
        String res = "";
        for(String s:spl) res+=s+" ";
        return res.trim();
    }

}
