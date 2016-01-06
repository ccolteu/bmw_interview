package com.bmw.interview;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bmw.interview.adapter.LocationsAdapter;
import com.bmw.interview.model.BMWLocation;
import com.bmw.interview.ui.MainActivity;
import com.bmw.interview.utils.MyObjectMapper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DisplayDataTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;

    public DisplayDataTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @Test
    public void loadData() {

        // load mock data
        final List<BMWLocation> locations = MyObjectMapper.parseLocations(openFileAsString("data.json"));

        // get list view
        final ListView listView = mActivity.getListView();
        assertNotNull("List not found", listView);

        // load data in list view
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(new LocationsAdapter(getActivity(), R.layout.location_row, locations));
            }
        });
        getInstrumentation().waitForIdleSync();

        // test loaded views match POJO data
        testNames(listView, locations);
    }

    private void testNames(ListView listView, List<BMWLocation> locations) {
        for (int position = 0; position < locations.size(); position++) {
            LinearLayout row = (LinearLayout) ((LocationsAdapter) listView.getAdapter()).getView(position, null, null);

            // only assert on the displayed rows
            if (row != null) {
                assertTrue(((TextView) row.findViewById(R.id.name)).getText().toString().equalsIgnoreCase(locations.get(position).getName()));
            }
        }
    }

    private String openFileAsString(String file) {
        return inputStreamToString(openFileAsInputStream(file));
    }

    private InputStream openFileAsInputStream(String file) {
        try {
            InputStream stream = getInstrumentation().getContext().getResources().getAssets().open(file);
            Assert.assertNotNull(stream);
            return stream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String inputStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
        }
        return out.toString();
    }
}
