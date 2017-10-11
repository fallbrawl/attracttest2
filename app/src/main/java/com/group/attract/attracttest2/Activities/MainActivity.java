package com.group.attract.attracttest2.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.group.attract.attracttest2.Fragments.ListFragment;
import com.group.attract.attracttest2.Fragments.RssFragment;
import com.group.attract.attracttest2.R;
import com.group.attract.attracttest2.Utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private ListFragment listFragments;
    private NavigationView nvDrawer;
    private RssFragment rssFragment;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(NetworkUtils.isNetworkAvailable(this)){
            // Set the initial fragment with list
            fragmentManager = getSupportFragmentManager();
            listFragments = new ListFragment();

            fragmentManager.beginTransaction().replace(R.id.flContent, listFragments).commit();

            // Set a Toolbar to replace the ActionBar.
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // Find our drawer view
            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = setupDrawerToggle();

            // Tie DrawerLayout events to the ActionBarToggle
            mDrawer.addDrawerListener(drawerToggle);

            // Find our drawer view
            nvDrawer = (NavigationView) findViewById(R.id.nvView);

            // Setup drawer view
            setupDrawerContent(nvDrawer);
        } else Toast.makeText(this, "NO INTERNETO!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);}


    public void selectDrawerItem (MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        rssFragment = new RssFragment();
        Fragment fragment;

        switch(menuItem.getItemId()) {
            case R.id.nav_list_fragment:
                fragment = listFragments;
                break;
            case R.id.nav_rss_channel_1:
                fragment = rssFragment;
                break;
            case R.id.nav_rss_channel_2:
                fragment = rssFragment;
                break;
            case R.id.nav_rss_channel_3:
                fragment = rssFragment;
                break;
            default:
                fragment = listFragments;
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }



}