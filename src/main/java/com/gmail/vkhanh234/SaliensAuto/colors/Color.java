package com.gmail.vkhanh234.SaliensAuto.colors;

public enum  Color {
    RESET("0","r"),
    GREEN("92","a"),
    AQUA("96","b"),
    RED("91","c"),
    LIGHT_PURPLE("95","d"),
    YELLOW("93","e");

    public String number,code;

    Color(String number, String code) {
        this.number = number;
        this.code = code;
    }


    public String getText(){
        return "\033["+getValue()+"m";
    }

    private String getValue() {
        return (this.equals(RESET)?"":"0;")+number;
    }

    public String getTag() {
        return "&"+code;
    }
}
