package com.mikhail.sportsnewshistoryrecords.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.mikhail.sportsnewshistoryrecords.MainActivity;
import com.mikhail.sportsnewshistoryrecords.R;
import com.mikhail.sportsnewshistoryrecords.api.NytAPI;
import com.mikhail.sportsnewshistoryrecords.api.NytSearchAPI;
import com.mikhail.sportsnewshistoryrecords.fragments.AllSportsFragment;
import com.mikhail.sportsnewshistoryrecords.fragments.NotificationFragment;
import com.mikhail.sportsnewshistoryrecords.model.NytSportsObjects;
import com.mikhail.sportsnewshistoryrecords.model.NytSportsResults;
import com.mikhail.sportsnewshistoryrecords.model.search.ArticleSearch;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Mikhail on 5/10/16.
 */
public class JobSchedulerService extends JobService {

    NytAPI latestNewsService;
    public ArrayList<NytSportsResults> articleLists;
    public String sections = "all";
    public String chooseMagazineSource = "all";
    private static final int NOTIFICATION_ID = 1;
    Context context;
    NytSportsResults nytSportsResults;
    SharedPreferences sharedPreferences;

    public static final String NYT_ALL = "Pro football,Pro basketball,baseball,soccer,Hockey";
    public static final String NYT_FOOTBALL = "Pro%20Football";
    public static final String NYT_BASKETBALL = "Pro basketball";
    public static final String NYT_BASEBALL = "baseball";
    public static final String NYT_HOCKEY = "Hockey";
    public static final String NYT_SOCCER = "Soccer";

    boolean topNewsCheck = false;
    boolean footballCheck = false;
    boolean basketballCheck = false;
    boolean baseballCheck = false;
    boolean hockeyCheck = false;
    boolean soccerCheck = false;
    boolean[] booleenArray;
    ArrayList<NytSportsObjects> sportsNewsList = new ArrayList<>();
    NotificationManager notificationManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Notifications", "StartCommand sent");

        /*
        booleenArray = intent.getBooleanArrayExtra(NotificationFragment.BOOLEAN_CODE);
        topNewsCheck = booleenArray[0];
        footballCheck = booleenArray[1];
        basketballCheck = booleenArray[2];
        baseballCheck = booleenArray[3];
        hockeyCheck = booleenArray[4];
        soccerCheck = booleenArray[5];
        */

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("Notifications", "onStartJob sent");

        context = getApplicationContext();


//        nytAllSportsNews();
//        spanishSoccerSearch();
        setApiCall();


        return false;

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * this method sets notifications
     * when system makes api call
     */
//    private void setNotifications() {
//
//        Intent intent = new Intent(context, MainActivity.class);
//
//        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//        mBuilder.setSmallIcon(R.drawable.ic_star_24dp);
//        mBuilder.setContentTitle("Notification from Excalibur!");
//        mBuilder.setContentText("New article: " + articleLists.get(0));
//        mBuilder.setContentIntent(pIntent);
//        mBuilder.setAutoCancel(true);
//        mBuilder.setPriority(Notification.PRIORITY_HIGH);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//
//    }
    private void createNotifications() {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_star_24dp);
        mBuilder.setContentTitle("New articles available!");
        mBuilder.setContentText("Hi");
        mBuilder.setContentIntent(pIntent);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setAutoCancel(true);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private void setApiCall() {
        Log.d("The Service", topNewsCheck + "");
        if (topNewsCheck) {
            nytAllSportsNews();
        } else if (footballCheck) {
            nytFootballNews();
        } else if (basketballCheck) {
            nytBasketballNews();
        } else if (baseballCheck) {
            nytBaseballNews();
        } else if (hockeyCheck) {
            nytHockeyNews();
        } else if (soccerCheck) {
            nytSoccerNews();
        }
    }

    private void spanishSoccerSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response("Spanish Soccer");

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                searchSportsResults.clear();
//                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
                Log.d("JobService!", "Service API MADE!");
                createNotifications();

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }


    public void nytAllSportsNews() {

        NytAPI.NytAPIRetrofit nytSports = NytAPI.create();

        Call<NytSportsResults> call = nytSports.response("all", "sports", NYT_ALL);

        call.enqueue(new Callback<NytSportsResults>() {
            @Override
            public void onResponse(Call<NytSportsResults> call, Response<NytSportsResults> response) {
                NytSportsResults nytSports = response.body();
                createNotifications();
            }

            @Override
            public void onFailure(Call<NytSportsResults> call, Throwable t) {

            }
        });
    }

    public void nytFootballNews() {

        NytAPI.NytAPIRetrofit nytSports = NytAPI.create();

        Call<NytSportsResults> call = nytSports.response("all", "sports", NYT_FOOTBALL);

        call.enqueue(new Callback<NytSportsResults>() {
            @Override
            public void onResponse(Call<NytSportsResults> call, Response<NytSportsResults> response) {
                NytSportsResults nytSports = response.body();
                createNotifications();
            }

            @Override
            public void onFailure(Call<NytSportsResults> call, Throwable t) {

            }
        });
    }

    public void nytBasketballNews() {

        NytAPI.NytAPIRetrofit nytSports = NytAPI.create();

        Call<NytSportsResults> call = nytSports.response("all", "sports", NYT_BASKETBALL);

        call.enqueue(new Callback<NytSportsResults>() {
            @Override
            public void onResponse(Call<NytSportsResults> call, Response<NytSportsResults> response) {
                NytSportsResults nytSports = response.body();
                createNotifications();
            }

            @Override
            public void onFailure(Call<NytSportsResults> call, Throwable t) {

            }
        });
    }


    public void nytBaseballNews() {

        NytAPI.NytAPIRetrofit nytSports = NytAPI.create();

        Call<NytSportsResults> call = nytSports.response("all", "sports", NYT_BASEBALL);

        call.enqueue(new Callback<NytSportsResults>() {
            @Override
            public void onResponse(Call<NytSportsResults> call, Response<NytSportsResults> response) {
                NytSportsResults nytSports = response.body();
                createNotifications();
            }

            @Override
            public void onFailure(Call<NytSportsResults> call, Throwable t) {

            }
        });
    }


    public void nytHockeyNews() {

        NytAPI.NytAPIRetrofit nytSports = NytAPI.create();

        Call<NytSportsResults> call = nytSports.response("all", "sports", NYT_HOCKEY);

        call.enqueue(new Callback<NytSportsResults>() {
            @Override
            public void onResponse(Call<NytSportsResults> call, Response<NytSportsResults> response) {
                NytSportsResults nytSports = response.body();
                createNotifications();
            }

            @Override
            public void onFailure(Call<NytSportsResults> call, Throwable t) {

            }
        });
    }

    public void nytSoccerNews() {

        NytAPI.NytAPIRetrofit nytSports = NytAPI.create();

        Call<NytSportsResults> call = nytSports.response("all", "sports", NYT_SOCCER);

        call.enqueue(new Callback<NytSportsResults>() {
            @Override
            public void onResponse(Call<NytSportsResults> call, Response<NytSportsResults> response) {
                NytSportsResults nytSports = response.body();
                createNotifications();
            }

            @Override
            public void onFailure(Call<NytSportsResults> call, Throwable t) {

            }
        });
    }

}

