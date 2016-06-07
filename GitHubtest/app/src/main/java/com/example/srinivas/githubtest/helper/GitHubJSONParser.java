package com.example.srinivas.githubtest.helper;

import android.util.Log;

import com.example.srinivas.githubtest.data.UserCommit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srinivas on 06-06-2016.
 */
public class GitHubJSONParser {

    public static List<UserCommit> toCommitList(String jsonArray) {
        try {
            List<UserCommit> commits = new ArrayList<>();
            JSONArray array = new JSONArray(jsonArray);
            JSONObject object;
            UserCommit commit;
            for (int i = 0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                commit=toUserCommit(object);
                if(commit!=null) commits.add(commit);
            }
            return commits;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserCommit toUserCommit(JSONObject jsonObject) {
        try {
            JSONObject author=jsonObject.getJSONObject("commit").getJSONObject("author");
            Log.i("author",author.toString());
            String name=author.getString("name");
            String email=author.getString("email");
            String date=author.getString("date");
            String message=jsonObject.getJSONObject("commit").getString("message");
            return new UserCommit(name,email,message,date);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
