package at.fhj.swd.travx.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Journey;
import at.fhj.swd.travx.ui.fragment.NothingAvailableFragment;
import at.fhj.swd.travx.ui.fragment.JourneyListFragment;
import at.fhj.swd.travx.util.ThreadUtils;

public class MainActivity extends AppCompatActivity {
    private NothingAvailableFragment nothingAvailableFragment;
    private JourneyListFragment journeyListFragment;

    private List<Journey> journeys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        Toolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.add) {
                Intent intent = new Intent(this, CreateJourneyActivity.class);
                startActivity(intent);
            }

            return true;
        });

        nothingAvailableFragment = new NothingAvailableFragment();
        journeyListFragment = new JourneyListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flFragmentContainer, journeyListFragment);
        transaction.add(R.id.flFragmentContainer, nothingAvailableFragment);
        transaction.hide(journeyListFragment);
        transaction.hide(nothingAvailableFragment);
        transaction.commit();

        setFragment();
        ThreadUtils.run(this::loadJourneys);
    }

    private void loadJourneys() {
        journeys = Database.getInstance(this).journeyDao().findAll();
        Collections.sort(journeys, (j1, j2) -> j2.getCreatedAt().compareTo(j1.getCreatedAt()));

        Log.i("NUMBER_OF_JOURNEYS", String.valueOf(journeys.size()));
        runOnUiThread(() -> journeyListFragment.update(journeys));
        setFragment();
    }

    private void setFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (journeys.size() > 0) {
            transaction.hide(nothingAvailableFragment);
            transaction.show(journeyListFragment);
        } else {
            transaction.hide(journeyListFragment);
            transaction.show(nothingAvailableFragment);
        }

        transaction.commit();
    }
}