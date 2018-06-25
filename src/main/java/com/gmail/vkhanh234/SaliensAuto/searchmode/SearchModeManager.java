package com.gmail.vkhanh234.SaliensAuto.searchmode;

import java.util.ArrayList;
import java.util.List;

public class SearchModeManager {
    public List<SearchMode> modes = new ArrayList<>();
    public SearchModeManager(){
        modes.add(new HighestCapturedRate());
        modes.add(new MostXpMode());
        modes.add(new FocusMode());
    }

    public SearchMode getSearchMode(int i){
        if(i<0 || i>=modes.size()) return null;
        return modes.get(i);
    }

}
