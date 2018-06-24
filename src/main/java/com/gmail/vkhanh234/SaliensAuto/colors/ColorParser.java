package com.gmail.vkhanh234.SaliensAuto.colors;

public class ColorParser {

    public static String parse(String s){
        for(Color color: Color.values()){
            if(s.contains("&"+color.code)) {
                s = s.replaceAll(color.getTag(), color.getText());
            }
        }
        return s;
    }
}
