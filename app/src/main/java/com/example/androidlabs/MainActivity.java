package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String STAR_WARS_API_URL = "https://swapi.dev/api/people/?format=json";
    private static final String TAG = "MainActivity";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ListView listView;
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        fetchStarWarsData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("details", details.get(position));

            if (findViewById(R.id.frameLayout) == null) {
                Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
            }
        });
    }

    private void fetchStarWarsData() {
        executorService.execute(() -> {
            try {
                String response = getResponseFromHttpUrl();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject character = results.getJSONObject(i);
                    names.add(character.getString("name"));
                    details.add(character.toString());
                }
                runOnUiThread(() -> listView.setAdapter(new CustomAdapter(MainActivity.this, names)));
            } catch (Exception e) {
                Log.e(TAG, "Error fetching Star Wars data", e);
            }
        });
    }

    private String getResponseFromHttpUrl() throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(MainActivity.STAR_WARS_API_URL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
