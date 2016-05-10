package com.mikhail.sportsnewshistoryrecords;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.mikhail.sportsnewshistoryrecords.adapters.ViewPagerAdapter;
import com.mikhail.sportsnewshistoryrecords.fragments.LeaguesFragment;
import com.mikhail.sportsnewshistoryrecords.fragments.SportsLeaguesArticleDetailViewFragment;

public class LeaguesActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SportsLeaguesArticleDetailViewFragment.ControlLeaguesActivityLayout,
        LeaguesFragment.LeaguesActivityControl {

    Toolbar toolbar;
    private int mNavigationItemId;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    Intent intent;
    public static final String RETURN_TO_MAIN_ACTIVITY = "backToMainActivity";
    SportsLeaguesArticleDetailViewFragment sportsLeaguesArticleDetailViewFragment;
    FrameLayout frameLayout;
    public int key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         key = getIntent().getIntExtra("KEY", 0);

        setContentView(R.layout.activity_leagues);
        toolbar = (Toolbar) findViewById(R.id.toolbar_leagues);
        setSupportActionBar(toolbar);

        intent = new Intent(LeaguesActivity.this, MainActivity.class);

        toolBarTitle();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_leagues);
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.addTab(tabLayout.newTab().setText("Records"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        frameLayout = (FrameLayout) findViewById(R.id.frag_container_leagues);

        fragmentManager = getSupportFragmentManager();

        viewPager = (ViewPager) findViewById(R.id.viewPager_leagues);
        viewPagerAdapter = new ViewPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPagerAdapter.setFragmentType(key);
        viewPager.setOffscreenPageLimit(2);

        Log.d("MainActivity", "Tab!" + tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
//        viewPager.setVisibility(View.GONE);
//        fragContainer.setVisibility(View.GONE);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_leagues);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_leagues);
        navigationView.setNavigationItemSelectedListener(this);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                int id = tab.getPosition();

//                if (id == 0) {
//                    viewPagerAdapter.setFragmentType(mNavigationItemId);
//
//                }

//                switch (id) {
//                    case 0:
//                        viewPagerAdapter.setFragmentType(mNavigationItemId);
//                        break;
//
//                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void setBundle(Bundle article) {
        sportsLeaguesArticleDetailViewFragment = new SportsLeaguesArticleDetailViewFragment();
        sportsLeaguesArticleDetailViewFragment.setArguments(article);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container_leagues, sportsLeaguesArticleDetailViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        viewPager.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_leagues);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//            intent.putExtra(RETURN_TO_MAIN_ACTIVITY, R.id.top_news);
//            startActivity(intent);
        } else if (sportsLeaguesArticleDetailViewFragment != null){
            if (frameLayout.getVisibility() == View.VISIBLE){
                frameLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                if (toolbar.getMenu().findItem(R.id.save_later) != null){
                    toolbar.getMenu().findItem(R.id.save_later).setVisible(false);
                }
                toolbar.getMenu().findItem(R.id.share).setVisible(false);
//                toolbar.getChildAt(2).findViewById(R.id.share).setVisibility(View.INVISIBLE);
            } else {
                int pos = viewPager.getCurrentItem();
                if (pos > 0) {
                    viewPager.setCurrentItem(pos - 1);
//                } else if (pos == 0){
//                    intent.putExtra(RETURN_TO_MAIN_ACTIVITY, R.id.top_news);
//                    startActivity(intent);
//                    finish();
//                    super.onBackPressed();
                }
            }
        }else {

            super.onBackPressed();
        }
    }


    @Override
    public void showViewPager(boolean visible) {
        if (visible) {
            viewPager.setVisibility(View.VISIBLE);
        } else {
            viewPager.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTabLayout(boolean visible) {

        if (visible) {
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int mNavigationItemId = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        mNavigationItemId = item.getItemId();
        viewPagerAdapter.setFragmentType(mNavigationItemId);

        switch (mNavigationItemId) {
            case R.id.top_news:
                // TODO send user back to main activty
                intent.putExtra(RETURN_TO_MAIN_ACTIVITY, R.id.top_news);
                startActivity(intent);

                break;
            case R.id.nfl:
                toolbar.setTitle("NFL Football");
                viewPagerAdapter.refreshData();
                break;
            case R.id.nba:
                toolbar.setTitle("NBA Basketball");
                viewPagerAdapter.refreshData();
                break;
            case R.id.mlb:
                toolbar.setTitle("MLB Baseball");
                viewPagerAdapter.refreshData();
                break;
            case R.id.nhl:
                toolbar.setTitle("NHL Hockey");
                viewPagerAdapter.refreshData();
                break;
            case R.id.mls:
                toolbar.setTitle("MLS Soccer");
                viewPagerAdapter.refreshData();
                break;
            case R.id.english_soccer:
                toolbar.setTitle("English Soccer");
                viewPagerAdapter.refreshData();
                break;
            case R.id.spanish_soccer:
                toolbar.setTitle("Spanish Soccer");
                viewPagerAdapter.refreshData();
                break;
            case R.id.italian_soccer:
                toolbar.setTitle("Italian Soccer");
                viewPagerAdapter.refreshData();
                break;
            case R.id.german_soccer:
                toolbar.setTitle("German Soccer");
                viewPagerAdapter.refreshData();
                break;
            case R.id.favorites:
//                TODO open saved articles fragment
                intent.putExtra(RETURN_TO_MAIN_ACTIVITY, R.id.favorites);
                startActivity(intent);
//                toolbar.setTitle("Saved stories");
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_leagues);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void toolBarTitle() {
        switch (key) {
            case R.id.nfl:
                toolbar.setTitle("NFL");
                break;
            case R.id.nba:
                toolbar.setTitle("NBA");
                break;
            case R.id.mlb:
                toolbar.setTitle("MLB");
                break;
            case R.id.nhl:
                toolbar.setTitle("NHL");
                break;
            case R.id.mls:
                toolbar.setTitle("MLS");
                break;
            case R.id.english_soccer:
                toolbar.setTitle("English Premier League");
                break;
            case R.id.spanish_soccer:
                toolbar.setTitle("Spanish La Liga");
                break;
            case R.id.italian_soccer:
                toolbar.setTitle("Italian Serie A");
                break;
            case R.id.german_soccer:
                toolbar.setTitle("German Bundesliga");
                break;
        }
    }

}
