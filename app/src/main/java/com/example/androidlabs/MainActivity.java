package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView; // ImageView to display cat images
    private ProgressBar progressBar; // ProgressBar to show loading status
    private static final String TAG = "MainActivity"; // Tag for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        // Start loading cat images
        loadCatImages();
    }

    private void loadCatImages() {
        new Thread(() -> {
            try {
                // Get random cat image metadata from the API
                URL url = new URL("https://cataas.com/cat?json=true");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();


                StringBuilder jsonResponse = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonResponse.append(line);
                }

                // Extract image ID from the JSON response
                String imageId = jsonResponse.substring(jsonResponse.indexOf("id\":\"") + 5, jsonResponse.indexOf("\",\"created_at\""));
                String imageUrl = "https://cataas.com/cat/" + imageId;

                // Check if the image is already saved locally
                File imageFile = new File(getApplicationContext().getFilesDir(), imageId + ".png");
                Bitmap catImageBitmap;

                if (imageFile.exists()) {
                    // Load the image from local storage
                    catImageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                } else {
                    // Download the image from the URL
                    url = new URL(imageUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    inputStream = connection.getInputStream();
                    catImageBitmap = BitmapFactory.decodeStream(inputStream);

                    // Save the image to local storage
                    FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                    catImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.close();
                }

                // Update the ImageView on the main thread
                runOnUiThread(() -> {
                    imageView.setImageBitmap(catImageBitmap);
                    progressBar.setVisibility(ProgressBar.GONE); // Hide progress bar after loading
                });

            } catch (Exception e) {
                Log.e(TAG, "Error loading cat images", e); // Use logging instead of printStackTrace
            }
        }).start(); // Start the thread
    }
}
