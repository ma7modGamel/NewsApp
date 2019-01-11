package com.example.xxpc.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.xxpc.newsapp.SettingsActivity.NewsAppsPreferenceFragment.numItem;

public class FirstActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<newsModel>> {

    TextView textNoConnection;
    ArrayList<newsModel> arrayListnews;
    RecyclerView recyclerNews;
    RecyclerView.LayoutManager manager;
    newsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        textNoConnection = findViewById(R.id.tvNoconnection);
        recyclerNews = findViewById(R.id.recycleId);
        mAdapter = new newsAdapter(getApplicationContext(), arrayListnews);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerNews.setLayoutManager(manager);
        initAdapter();
        if (isNetworkAvailable(getApplicationContext())) {
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            recyclerNews.setVisibility(View.GONE);
            textNoConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(settingsIntent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String numItemString = SP.getString(getString(R.string.settings_min_num_key),getString(R.string.settings_min_numItem_default));
                numItem = Integer.valueOf(numItemString);
                restartApp();
                }
            }
        }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void restartApp() {
        Toast.makeText(getApplicationContext(), R.string.restart, Toast.LENGTH_SHORT).show();
        Intent i = getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(getApplicationContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finishAffinity();
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void initAdapter() {
        arrayListnews = new ArrayList<>();
        recyclerNews.setAdapter(new newsAdapter(getApplicationContext(), arrayListnews));
    }

    @Override
    public Loader<ArrayList<newsModel>> onCreateLoader(int id, Bundle args) {
        FetchNewsData asyncTaskLoader = new FetchNewsData(recyclerNews, this);
        asyncTaskLoader.forceLoad();
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<newsModel>> loader, ArrayList<newsModel> data) {


    if (!data.isEmpty()&&data != null) {
        mAdapter.setNews(data);
        mAdapter.notifyDataSetChanged();
        recyclerNews.setAdapter(mAdapter);
    } else {
        recyclerNews.setVisibility(View.GONE);
        textNoConnection.setVisibility(View.VISIBLE);
        textNoConnection.setText(R.string.noNews);
        textNoConnection.setTextSize(25);
        textNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<newsModel>> loader) {
        recyclerNews.setAdapter(null);
    }
}
