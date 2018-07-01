package com.gmail.vkhanh234.SaliensAuto.data;

import com.gmail.vkhanh234.SaliensAuto.Main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestResult {
    public String data,errorMessage;
    public int responseCode=-1,eResult=-1;
    public void parseData(HttpsURLConnection conn) throws IOException {
        BufferedReader in=null;
        responseCode = conn.getResponseCode();

        eResult = getEResult(conn);
        errorMessage = getErrorMessage(conn);

        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        data=result.toString();
        if(in!=null){
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    private int getEResult(HttpsURLConnection conn) {
        String s = conn.getHeaderField("x-eresult");
        if(s==null) s = conn.getHeaderField("X-eresult");
        if(s==null) return -1;
        return Integer.valueOf(s);
    }

    private String getErrorMessage(HttpsURLConnection conn) {
        String s = conn.getHeaderField("x-error_message");
        if(s==null) s = conn.getHeaderField("X-error_message");
        return convertErrorMessage(eResult,s);
    }

    private static String convertErrorMessage(int eresult, String errorMessage) {
        if(eresult==17) return "Steam doesn't allow us to join this zone";
        return errorMessage;
    }

}
