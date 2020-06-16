package at.fhj.swd.travx.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.domain.Journey;
import at.fhj.swd.travx.ui.fragment.JourneyListFragment;

public class MainActivity extends AppCompatActivity {

    private List<Journey> journeys = new ArrayList<Journey>() {{
        add(new Journey("Island"));
        add(new Journey("Irland"));
        add(new Journey("Klettern 2020"));
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
                System.out.println("test"); //TODO
                return true;
            }

            return false;
        });

        JourneyListFragment journeyListFragment = new JourneyListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flFragmentContainer, journeyListFragment);
        transaction.commit();

        new Thread(() -> { // TODO check alternative (maybe not needed anymore if DB connection is established
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> journeyListFragment.update(journeys));
        }).start();
    }
}