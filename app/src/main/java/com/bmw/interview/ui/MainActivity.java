package com.bmw.interview.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ListView;

import com.bmw.interview.R;

public class MainActivity extends FragmentActivity {
    private static final String MAIN_FRAGMENT_TAG = "main_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        using a fragment to easily retain data and state across configuration changes
        such as rotation while executing async operations (GET, POST)
         */
        FragmentManager fm = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) fm.findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new MainFragment();
        }
        if (!fragment.isAdded()) {
            fm.beginTransaction().add(R.id.content, fragment, MAIN_FRAGMENT_TAG).commit();
        }
    }

    public ListView getListView() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (f != null) {
            return (ListView) f.getView().findViewById(R.id.list);
        }
        return null;
    }

}
