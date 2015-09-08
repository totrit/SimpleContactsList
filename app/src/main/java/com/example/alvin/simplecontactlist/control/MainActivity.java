package com.example.alvin.simplecontactlist.control;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.alvin.simplecontactlist.R;
import com.example.alvin.simplecontactlist.model.ContactsDataManager;
import com.example.alvin.simplecontactlist.utils.LogUtils;
import com.example.alvin.simplecontactlist.view.ContactDetailFrag;
import com.example.alvin.simplecontactlist.view.MainFrag;


public class MainActivity extends ActionBarActivity implements ITopLevelDelegate {
    private final static String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsDataManager.getInstance().setAppContext(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        showMainList();
    }

    @Override
    public void displayContact(int id) {
        ContactDetailFrag newFragment = ContactDetailFrag.createInstance(id);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_push_in, R.anim.fragment_push_out, R.anim.fragment_pop_in, R.anim.fragment_pop_out);
        ft.replace(R.id.frag_placeholder, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void showMainList() {
        MainFrag newFragment = new MainFrag();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frag_placeholder, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void changeTitle(String newTitle) {
        this.getSupportActionBar().setTitle(newTitle);
    }

    @Override
    public void showBackButton(boolean show) {
        ActionBar actionBar = getSupportActionBar();
        if (show) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        LogUtils.d(LOG_TAG, "back stack depth: " + getFragmentManager().getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home || id == R.id.up || id == R.id.homeAsUp) {
            goBack();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            Fragment lastFragment = getFragmentManager().findFragmentById(getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1)
                    .getId());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frag_placeholder, lastFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }

}
