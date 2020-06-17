package at.fhj.swd.travx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import at.fhj.swd.travx.R;

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

        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.add) {
                Log.i("CREATE_JOURNEY", "create journey..."); //TODO impl
            }

            return false;
        });
    }
}
