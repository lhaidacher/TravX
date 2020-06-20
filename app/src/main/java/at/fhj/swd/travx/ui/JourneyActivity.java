package at.fhj.swd.travx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.domain.Journey;
import at.fhj.swd.travx.domain.JourneyStats;
import at.fhj.swd.travx.ui.fragment.BillListFragment;
import at.fhj.swd.travx.util.CurrencyUtils;
import at.fhj.swd.travx.util.DateUtils;
import at.fhj.swd.travx.util.ThreadUtils;

public class JourneyActivity extends AppCompatActivity {
    private Journey journey;

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvBudget;
    private TextView tvCreated;
    private TextView tvUsedBudget;
    private TextView tvHighestBill;
    private TextView tvLowestBill;
    private TextView tvAverage;
    private TextView tvLastBill;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_journey);

        tvTitle = findViewById(R.id.tvJourneyName);
        tvDescription = findViewById(R.id.tvJourneyDescription);
        tvBudget = findViewById(R.id.tvJourneyBudget);
        tvCreated = findViewById(R.id.tvJourneyCreated);
        tvUsedBudget = findViewById(R.id.tvJourneyBudgetUsed);
        tvHighestBill = findViewById(R.id.tvJourneyBudgetMaxBill);
        tvLowestBill = findViewById(R.id.tvJourneyBudgetMinBill);
        tvAverage = findViewById(R.id.tvJourneyBudgetAverage);
        tvLastBill = findViewById(R.id.tvJourneyBudgetLastBill);

        Toolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add) {
                ThreadUtils.run(this::doScan);
            } else if (item.getItemId() == R.id.remove) {
                ThreadUtils.run(this::doDelete);
            } else if (item.getItemId() == R.id.view) {
                ThreadUtils.run(this::doView);
            }
            return true;
        });

        ThreadUtils.run(this::setContent);
    }

    private void setContent() {
        journey = Database.getInstance(this)
                .journeyDao()
                .findByName(getIntent().getStringExtra("journey_name"));

        List<Bill> bills = Database.getInstance(this)
                .billDao()
                .findAllByJourneyTitle(journey.getTitle());
        JourneyStats stats = new JourneyStats(bills);

        Log.i("JOURNEY_NAME", journey.getTitle());
        Log.i("JOURNEY_BILL_SIZE", String.valueOf(bills.size()));

        runOnUiThread(() -> {
            tvTitle.setText(journey.getTitle());
            tvDescription.setText(journey.getDescription());
            tvBudget.setText(CurrencyUtils.format(journey.getBudget()));
            tvCreated.setText(DateUtils.format(journey.getCreatedAt()));

            if (stats.available()) {
                tvUsedBudget.setText(String.format(Locale.GERMANY, "%s with %d bill%s", CurrencyUtils.format(stats.getSum()), bills.size(), bills.size() > 1 ? "s" : ""));
                tvHighestBill.setText(CurrencyUtils.format(stats.getHighest().getValue()));
                tvLowestBill.setText(CurrencyUtils.format(stats.getLowest().getValue()));
                tvAverage.setText(CurrencyUtils.format(stats.getAvg()));
                tvLastBill.setText(String.format("%s @ %s", CurrencyUtils.format(stats.getLast().getValue()), DateUtils.format(stats.getLast().getCreatedAt())));
            }

        });
    }

    private void doDelete() {
        Log.i("DELETE_JOURNEY", journey.getTitle());

        Database.getInstance(this)
                .journeyDao()
                .remove(journey.getTitle());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void doScan() {
        Intent intent = new Intent(this, BillCaptureActivity.class);
        intent.putExtra("journey_name", journey.getTitle());
        startActivity(intent);
    }

    private void doView() {
        Intent intent = new Intent(this, BillListActivity.class);
        intent.putExtra("journey_name", journey.getTitle());
        startActivity(intent);
    }
}
