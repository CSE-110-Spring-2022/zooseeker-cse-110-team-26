package com.example.zooapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
//import androidx.test.espresso.remote.EspressoRemoteMessage;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TestSearchBar {
    ExhibitsDatabase testDb;
    ExhibitsItemDao ExhibitsItemDao;
    Context context;

    @Before
    public void resetDatabase() {
        context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitsDatabase.class)
                .allowMainThreadQueries()
                .build();
       ExhibitsDatabase.injectTestDatabase(testDb);

        List<ExhibitsItem> todos = ExhibitsItem.loadJSON(context, "sample_node_info.json");
        ExhibitsItemDao = testDb.exhibitsItemDao();
        ExhibitsItemDao.insertAll(todos);
    }

    @Test
    public void testPopUp() {
        ActivityScenario<ExhibitsActivity> scenario =
                ActivityScenario.launch(ExhibitsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);
        scenario.onActivity(activity -> {
            AutoCompleteTextView auto = activity.autoComplete;
            EditText editText = activity.findViewById(R.id.search_bar);
            String testText = "";

            editText.setText(testText);

            boolean popUp = false;
            assertEquals(popUp, auto.isPopupShowing());

        });
    }
    @Test
    public void testResultCount() {
        ActivityScenario<ExhibitsActivity> scenario =
                ActivityScenario.launch(ExhibitsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);
        scenario.onActivity(activity -> {
            AutoCompleteTextView auto = activity.autoComplete;
            EditText editText = activity.findViewById(R.id.search_bar);
            String testText = "a";

            Adapter adapter = auto.getAdapter();
            editText.setText(testText);


            int key = adapter.getCount();
            assertEquals(12, key);

        });
    }

}
