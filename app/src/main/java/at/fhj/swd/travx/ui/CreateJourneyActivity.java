package at.fhj.swd.travx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Random;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.dao.Database;
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.domain.Journey;
import at.fhj.swd.travx.util.InputUtils;
import at.fhj.swd.travx.util.ThreadUtils;

public class CreateJourneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_create_journey);

        Toolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add) {
                ThreadUtils.run(this::doSave);
            }
            return true;
        });
    }

    private void doSave() {
        String tripName = InputUtils.getText(findViewById(R.id.tfTripName));
        String tripDescription = InputUtils.getText(findViewById(R.id.tfTripDescription));
        Long tripBudget = InputUtils.getLong(findViewById(R.id.tfTripBudget));

        Journey journey = new Journey(tripName, tripDescription, tripBudget);

        if (isValid(journey)) {
            Log.i("NEW_JOURNEY", tripName);

            Database.getInstance(this)
                    .journeyDao()
                    .add(journey);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean isValid(Journey journey) {
        clearErrors();
        boolean isValid = true;

        if (journey.getTitle().isEmpty()) {
            setEmptyStringError(findViewById(R.id.tfTripName), "trip name");
            isValid = false;
        } else if (journey.getTitle().length() > 30) {
            setTooLongStringError(findViewById(R.id.tfTripName), "Trip name");
            isValid = false;
        }

        if (journey.getDescription().isEmpty()) {
            setEmptyStringError(findViewById(R.id.tfTripDescription), "trip description");
            isValid = false;
        } else if (journey.getDescription().length() > 30) {
            setTooLongStringError(findViewById(R.id.tfTripDescription), "Trip description");
            isValid = false;
        }

        return isValid;
    }

    private void clearErrors() {
        clearError(findViewById(R.id.tfTripName));
        clearError(findViewById(R.id.tfTripDescription));
    }

    private void clearError(TextInputLayout input) {
        runOnUiThread(() -> input.setError(null));
    }

    private void setEmptyStringError(TextInputLayout input, String inputName) {
        runOnUiThread(() -> input.setError(String.format("No %s provided", inputName)));
    }

    private void setTooLongStringError(TextInputLayout input, String inputName) {
        runOnUiThread(() -> input.setError(String.format("%s too long - max. 30.", inputName)));
    }
}
