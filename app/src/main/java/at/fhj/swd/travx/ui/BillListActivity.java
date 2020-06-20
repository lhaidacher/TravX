package at.fhj.swd.travx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.ui.fragment.BillListFragment;
import at.fhj.swd.travx.ui.fragment.NothingAvailableFragment;
import at.fhj.swd.travx.util.ThreadUtils;

public class BillListActivity extends AppCompatActivity {
    private NothingAvailableFragment nothingAvailableFragment;
    private BillListFragment billListFragment;
    private String journeyName;
    private List<Bill> bills = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.bill_list_activity);

        journeyName = getIntent().getStringExtra("journey_name");

        Toolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, JourneyActivity.class);
            intent.putExtra("journey_name", journeyName);
            startActivity(intent);
        });

        billListFragment = new BillListFragment();
        nothingAvailableFragment = new NothingAvailableFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flFragmentContainer, billListFragment);
        transaction.add(R.id.flFragmentContainer, nothingAvailableFragment);
        transaction.hide(billListFragment);
        transaction.hide(nothingAvailableFragment);
        transaction.commit();

        setFragment();
        ThreadUtils.run(this::loadBills);
    }

    private void loadBills() {
        bills = Database.getInstance(this)
                .billDao()
                .findAllByJourneyTitle(journeyName);
        Collections.sort(bills, (b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()));
        Log.i("NUMBER_OF_BILLS", String.valueOf(bills.size()));
        runOnUiThread(() -> billListFragment.update(bills));
        setFragment();
    }

    private void setFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (bills.size() > 0) {
            transaction.show(billListFragment);
            transaction.hide(nothingAvailableFragment);
        } else {
            transaction.show(nothingAvailableFragment);
            transaction.hide(billListFragment);
        }

        transaction.commit();
    }
}
