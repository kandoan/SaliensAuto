package com.gmail.vkhanh234.SaliensAuto.data.Planet;

import java.util.List;

public class Planets {
    public List<Planet> planets;

    public String getTopPriorityPlanet() {
        int min = Integer.MAX_VALUE;
        String id="1";
        for(Planet planet:planets){
            if(planet.state==null || !planet.state.active || planet.state.captured) continue;
            if(min>planet.state.priority){
                min = planet.state.priority;
                id=planet.id;
            }
        }
        return id;
    }
    public String getTopPriorityPlanet2() {
        int min = Integer.MAX_VALUE;
        String id="1";
        for(Planet planet:planets){
            if(planet.state==null || !planet.state.active || planet.state.captured) continue;
            if(min>planet.state.priority){
                min = planet.state.priority;
                id=planet.id;
            }
        }
        return id;
    }
}
