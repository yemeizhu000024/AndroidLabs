package com.example.androidlabs;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle bundle = getIntent().getExtras();
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
