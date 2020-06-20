package at.fhj.swd.travx.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.github.javafaker.Faker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Random;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.network.HttpsGetTask;
import at.fhj.swd.travx.network.RequestCallback;
import at.fhj.swd.travx.service.LocationService;
import at.fhj.swd.travx.util.ThreadUtils;

public class BillCaptureActivity extends AppCompatActivity implements RequestCallback {
    private String journeyName;
    private ImageView billImage;
    private Bitmap image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.bill_capture_activity);

        billImage = findViewById(R.id.bill_image);
        journeyName = getIntent().getStringExtra("journey_name");

        Toolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view -> {
            stopLocationService();
            Intent intent = new Intent(this, JourneyActivity.class);
            intent.putExtra("journey_name", journeyName);
            startActivity(intent);
        });

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.capture) {
                selectImage(this);
            } else if (item.getItemId() == R.id.add) {
                ThreadUtils.run(this::processImage);
            }
            return true;
        });

        startLocationService();
    }

    /**
     * The idea is, that image is sent to server, where the image is processed via computer vision and
     * image recognition to get sum of bill, which needs to be payed. For this app, this functionality
     * would be an over-kill, so there is an API call, which just retrieves an JSON with and random
     * value used as bill value. If the call does not work, due to fact that the API is not available, a
     * random number is generated.
     */
    private void processImage() {
        if (image == null) {
            showErrorMessage("Please capture an image.");
            return;
        }

        // idea: add image to request, but due to performance reasons skipped!
        HttpsGetTask httpsGetTask = new HttpsGetTask(this);
        httpsGetTask.execute(String.format("https://api.agify.io/?name=%s", new Faker().name().firstName()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK && data != null) {
                    image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    billImage.setImageBitmap(image);
                }
            }
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your bill picture");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onResult(String result) {
        new Thread(() -> {
            Long value;

            if (result == null) {
                value = getRandomNumber();
            } else {
                value = parseBillValue(result);
                if (value == null) {
                    value = getRandomNumber();
                }
            }

            update(value);
        }).start();
    }

    private Long parseBillValue(String json) {
        Long value = null;

        try {
            value = new JSONObject(json).getLong("age");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }

    private void update(Long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        float latitude = preferences.getFloat("latitude", -1000);
        float longitude = preferences.getFloat("longitude", -1000);

        if (latitude == -1000 || longitude == -1000) {
            showErrorMessage("Can not get location - try again!");
        } else {
            long id = Database.getInstance(this)
                    .billDao()
                    .add(new Bill(journeyName, value, latitude, longitude));

            stopLocationService();
            runOnUiThread(() -> {
                Intent intent = new Intent(this, BillActivity.class);
                intent.putExtra("journey_name", journeyName);
                intent.putExtra("bill_id", id);
                //startActivity(intent); //TODO start redirect
            });
        }
    }

    private void startLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            }
        }
    }

    private void stopLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
    }

    @Override
    public void onRequestStart() {
        Log.i("REQUEST_STARTS", "request is sent to API");
    }

    private void showErrorMessage(String message) {
        runOnUiThread(() -> {
            final CharSequence[] options = {"OK"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(message);

            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals("OK")) {
                    dialog.dismiss();
                }
            });

            builder.show();
        });
    }

    private Long getRandomNumber() {
        return Math.abs(new Random().nextLong()) % 151;
    }
}
