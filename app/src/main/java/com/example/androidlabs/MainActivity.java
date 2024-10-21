package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<ToDoItem> toDoList = new ArrayList<>();
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.todo_list);
        EditText editText = findViewById(R.id.todo_input);
        SwitchCompat urgentSwitch = findViewById(R.id.urgent_switch);
        Button addButton = findViewById(R.id.add_button);

        adapter = new ToDoAdapter(toDoList, this);
        listView.setAdapter(adapter);

        // Add button click listener
        addButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                boolean isUrgent = urgentSwitch.isChecked();
                toDoList.add(new ToDoItem(text, isUrgent));
                editText.setText("");  // Clear EditText
                adapter.notifyDataSetChanged();  // Refresh ListView
            } else {
                Toast.makeText(this, "Please type something!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set long click listener to delete item
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        toDoList.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }
}