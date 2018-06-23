package com.gmail.vkhanh234.SaliensAuto;
import com.gmail.vkhanh234.SaliensAuto.data.Github.GithubRelease;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.String;

public class VersionUtils {
    public static String getLocalVersion(){
        return "v"+Main.class.getPackage().getImplementationVersion();
    }

    public static String getRemoteVersion(){
        String data = RequestUtils.githubApi("releases/latest");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<GithubRelease> jsonAdapter = moshi.adapter(GithubRelease.class);
        try {
            GithubRelease release = jsonAdapter.fromJson(data);
            return release.tag_name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isOutdatedVersion(){
        return !getLocalVersion().equalsIgnoreCase(getRemoteVersion());
    }
}
