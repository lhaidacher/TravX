package at.fhj.swd.travx.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.domain.Journey;
import at.fhj.swd.travx.ui.fragment.EmptyJourneyListFragment;
import at.fhj.swd.travx.ui.fragment.JourneyListFragment;

public class MainActivity extends AppCompatActivity {
    private EmptyJourneyListFragment emptyJourneyListFragment;
    private JourneyListFragment journeyListFragment;

    private List<Journey> journeys = new ArrayList<Journey>() {{
        //add(new Journey("Island"));
        //add(new Journey("Irland"));
        //add(new Journey("Klettern 2020"));
    }};


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

        emptyJourneyListFragment = new EmptyJourneyListFragment();
        journeyListFragment = new JourneyListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flFragmentContainer, journeyListFragment);
        transaction.add(R.id.flFragmentContainer, emptyJourneyListFragment);
        transaction.hide(journeyListFragment);
        transaction.hide(emptyJourneyListFragment);
        transaction.commit();

        new Thread(() -> { // TODO check alternative (maybe not needed anymore if DB connection is established
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> journeyListFragment.update(journeys));
        }).start();

        setFragment(journeys.size());
    }

    private void setFragment(int journeys) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (journeys > 0) {
            transaction.hide(emptyJourneyListFragment);
            transaction.show(journeyListFragment);
        } else {
            transaction.hide(journeyListFragment);
            transaction.show(emptyJourneyListFragment);
        }

        transaction.commit();
    }
}