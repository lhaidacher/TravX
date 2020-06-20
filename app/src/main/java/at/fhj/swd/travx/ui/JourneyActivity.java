package at.fhj.swd.travx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Journey;
import at.fhj.swd.travx.util.ThreadUtils;

public class JourneyActivity extends AppCompatActivity {
    private Journey journey;

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvBudget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_journey);

        tvTitle = findViewById(R.id.tvJourneyName);
        tvDescription = findViewById(R.id.tvJourneyDescription);
        tvBudget = findViewById(R.id.tvJourneyBudget);

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
            }
            return true;
        });

        ThreadUtils.run(this::setContent);
    }

    private void setContent() {
        journey = Database.getInstance(this)
                .journeyDao()
                .findByName(getIntent().getStringExtra("journey_name"));

        Log.i("JOURNEY_NAME", journey.getTitle());

        runOnUiThread(() -> {
            tvTitle.setText(journey.getTitle());
            tvDescription.setText(journey.getDescription());
            tvBudget.setText(NumberFormat.getCurrencyInstance(Locale.GERMANY).format(journey.getBudget()));
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
        // TODO
    }
}
