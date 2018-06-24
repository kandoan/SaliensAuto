package com.gmail.vkhanh234.SaliensAuto;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;


public class RequestUtils {
    public static String post(String type, String dat) {
        if(Main.token==null){
            Main.debug("&eError:&r Token hasn't been set yet");
            return null;
        }
        try {
            trustAllHosts();
            if(dat.length()>0) dat+="&access_token="+Main.token;
            else dat="access_token="+Main.token;
            byte[] postData = dat.getBytes(StandardCharsets.UTF_8);
            URL url = new URL("https://community.steam-api.com/"+type+"/v0001/");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Origin", "https://steamcommunity.com");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Referer", "https://steamcommunity.com/saliengame/play");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            if (conn.getResponseCode() != 200) return null;
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            conn.disconnect();
            in.close();
            return result.toString();
        } catch (IOException e) {
            Main.debug("&aError: &rCan't connect to Steam Server. Server down? Let's wait a little while.");
//            e.printStackTrace();
            return null;
        }
    }
    public static String get(String type, String dat) {
        try {
            trustAllHosts();
            URL url = new URL("https://community.steam-api.com/ITerritoryControlMinigameService/"+type+"/v0001/?"+dat);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Origin", "https://steamcommunity.com");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Referer", "https://steamcommunity.com/saliengame/play");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
            conn.setUseCaches(false);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
            if (conn.getResponseCode() != 200) return null;
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            conn.disconnect();
            in.close();
            return result.toString();
        } catch (IOException e) {
//            e.printStackTrace();
            Main.debug("&aError: &rCan't connect to Steam Server. Server down? Let's wait a little while.");
            return null;
        }
    }

    public static String githubApi(String text) {
        try {
            trustAllHosts();
            URL url = new URL("https://api.github.com/repos/KickVN/SaliensAuto/"+text);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
            conn.setUseCaches(false);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
            if (conn.getResponseCode() != 200) return null;
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            conn.disconnect();
            in.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Copied from stackoverflow lol
    public static void trustAllHosts()
    {
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509ExtendedTrustManager()
                    {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {
                        }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {
                        }
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {
                        }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {
                        }
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new  HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {
        }
    }
}
