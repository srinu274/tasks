package com.example.srinivas.githubtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.srinivas.githubtest.customview.CommitGroupView;
import com.example.srinivas.githubtest.data.UserCommit;
import com.example.srinivas.githubtest.data.UserCommitGroup;
import com.example.srinivas.githubtest.helper.GitHubJSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(new CommitListAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new CommitGetTask().execute();
    }


    private class CommitListAdapter extends RecyclerView.Adapter<CommitListHolder> {

        private List<UserCommitGroup> groups;

        @Override
        public CommitListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.list_commits,parent,false);
            return new CommitListHolder(v);
        }

        @Override
        public void onBindViewHolder(CommitListHolder holder, int position) {
            holder.setView(groups.get(position));
        }

        @Override
        public int getItemCount() {
            return groups!=null?groups.size():0;
        }

        public void setGroups(List<UserCommitGroup> groups) {
            this.groups=groups;
            notifyDataSetChanged();
        }

        public List<UserCommitGroup> getGroups() {
            return groups;
        }
    }


    private class CommitListHolder extends RecyclerView.ViewHolder {

        CommitGroupView view;

        public CommitListHolder(View itemView) {
            super(itemView);
            view=(CommitGroupView) itemView.findViewById(R.id.groupView);
        }

        public void setView(UserCommitGroup group) {
            view.setCommits(MainActivity.this,group.getCommits());
        }
    }


    public List<UserCommit> getCommits() {
        HttpsURLConnection connection=null;
        try {
            URL url=new URL("https://api.github.com/repos/rails/rails/commits"+"?since"+"2016-04-01T00:00:00Z");
            connection=(HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                BufferedInputStream stream = new BufferedInputStream(connection.getInputStream());
                int ch = -1;
                StringBuilder builder = new StringBuilder();
                while ((ch = stream.read()) != -1) {
                    builder.append((char)ch);
                }
                return GitHubJSONParser.toCommitList(builder.toString());
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private class CommitGetTask extends AsyncTask<Void,Void,List<UserCommitGroup>> {

        @Override
        protected List<UserCommitGroup> doInBackground(Void... params) {
            List<UserCommit> commits=getCommits();
            if(commits==null) return null;
            return UserCommitGroup.getCommitGroups(commits);
        }

        @Override
        protected void onPostExecute(List<UserCommitGroup> result){
            Log.i("commits",result+"");
            ((CommitListAdapter)mRecyclerView.getAdapter()).setGroups(result);
        }
    }
}
