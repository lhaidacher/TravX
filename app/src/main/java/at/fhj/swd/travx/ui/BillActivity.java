package at.fhj.swd.travx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.util.CurrencyUtils;
import at.fhj.swd.travx.util.DateUtils;
import at.fhj.swd.travx.util.StringUtils;
import at.fhj.swd.travx.util.ThreadUtils;

public class BillActivity extends AppCompatActivity {
    private Long billId;
    private String journeyName;

    private TextView tvValue;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvCreated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_bill);

        journeyName = getIntent().getStringExtra("journey_name");
        billId = getIntent().getLongExtra("bill_id", -1);

        tvValue = findViewById(R.id.tvBillValue);
        tvLatitude = findViewById(R.id.tvBillLat);
        tvLongitude = findViewById(R.id.tvBillLon);
        tvCreated = findViewById(R.id.tvBillCreated);

        Toolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra("journey_name", journeyName);
            startActivity(intent);
        });

        ThreadUtils.run(this::setContent);
    }

    private void setContent() {
        Bill bill = Database.getInstance(this)
                .billDao()
                .findById(billId);

        Log.i("JOURNEY_NAME", bill.getJourneyTitle());
        Log.i("BILL_VALUE", bill.getValue().toString());

        runOnUiThread(() -> {
            tvValue.setText(CurrencyUtils.format(bill.getValue()));
            tvLatitude.setText(StringUtils.format(bill.getLatitude()));
            tvLongitude.setText(StringUtils.format(bill.getLongitude()));
            tvCreated.setText(DateUtils.formatExtended(bill.getCreatedAt()));
        });
    }
}
