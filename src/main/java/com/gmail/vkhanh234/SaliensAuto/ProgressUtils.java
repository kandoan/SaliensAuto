package com.gmail.vkhanh234.SaliensAuto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProgressUtils {

    public static String getTimeLeft(int amount, int divide) {
        int secs = amount/divide;
        if(secs<60) return secs+"s";
        int mins = secs/60;
        if(mins<60) return mins+"m "+secs%60+"s";
        int hours = mins/60;
        if(hours<24) return hours+"h "+mins%60+"m";
        return hours/24+"d "+hours%24+"h "+mins%60+"m";
    }

    public static double getPercent(int score, int total){
        return round((1.0*score/total)*100,2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
