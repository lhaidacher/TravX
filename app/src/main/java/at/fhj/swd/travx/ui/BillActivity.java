package at.fhj.swd.travx.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.util.CurrencyUtils;
import at.fhj.swd.travx.util.DateUtils;
import at.fhj.swd.travx.util.StringUtils;
import at.fhj.swd.travx.util.ThreadUtils;

public class BillActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private Long billId;
    private Bill bill;
    private String journeyName;

    private TextView tvValue;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvCreated;

    private MapView mapView;
    private GoogleMap map;

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

        mapView = findViewById(R.id.billMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Toolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra("journey_name", journeyName);
            startActivity(intent);
        });

        ThreadUtils.run(this::setContent);
    }

    private void setContent() {
        bill = Database.getInstance(this)
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

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            map.setMyLocationEnabled(true);

        map.clear();
        MarkerOptions markerOptions = new MarkerOptions()
                .title("Bill")
                .position(new LatLng(bill.getLatitude(), bill.getLongitude()));
        map.addMarker(markerOptions);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        Log.i("LIFECYCLE", "onPause map");
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        Log.i("LIFECYCLE", "onStop map");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Log.i("LIFECYCLE", "onDestroy map");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
