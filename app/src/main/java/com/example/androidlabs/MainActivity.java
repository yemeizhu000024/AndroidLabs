package com.example.androidlabs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private TodoDatabaseHelper dbHelper;  // Declare the database helper

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

        // Instantiate TodoDatabaseHelper
        dbHelper = new TodoDatabaseHelper(this);

        // Load saved todos
        loadTodosFromDatabase();

        // Add button click listener
        addButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                boolean isUrgent = urgentSwitch.isChecked();
                // Add new task to the database
                addTodoToDatabase(text, isUrgent);
                editText.setText("");  // Clear EditText
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
                        // Delete todo item from the database
                        deleteTodoFromDatabase(position);
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void loadTodosFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TodoDatabaseHelper.TABLE_TODOS, null, null, null, null, null, null);

        if (cursor != null) {
            // Clear the existing list to avoid duplicate entries
            toDoList.clear();

            // Iterate through cursor and add tasks
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String task = cursor.getString(cursor.getColumnIndex(TodoDatabaseHelper.COLUMN_TASK));
                @SuppressLint("Range") int urgency = cursor.getInt(cursor.getColumnIndex(TodoDatabaseHelper.COLUMN_URGENCY));
                toDoList.add(new ToDoItem(task, urgency == 1)); // Convert urgency to boolean
            }
            cursor.close(); // Close cursor
            adapter.notifyDataSetChanged(); // Refresh ListView after loading
        }
    }

    private void addTodoToDatabase(String task, boolean isUrgent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoDatabaseHelper.COLUMN_TASK, task);
        values.put(TodoDatabaseHelper.COLUMN_URGENCY, isUrgent ? 1 : 0);  // Convert boolean to int

        long newRowId = db.insert(TodoDatabaseHelper.TABLE_TODOS, null, values); // Insert the new row and get its ID
        if (newRowId != -1) { // Check if insertion was successful
            Toast.makeText(this, "Todo added!", Toast.LENGTH_SHORT).show();
            loadTodosFromDatabase(); // Reload todos from the database
        } else {
            Toast.makeText(this, "Error adding todo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteTodoFromDatabase(int position) {
        ToDoItem itemToDelete = toDoList.get(position);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete todo item from the database
        db.execSQL("DELETE FROM " + TodoDatabaseHelper.TABLE_TODOS + " WHERE " +
                        TodoDatabaseHelper.COLUMN_TASK + " = ?",
                new Object[]{itemToDelete.getText()});  // Use task name for deletion

        // Remove from the list and notify adapter
        toDoList.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Todo deleted!", Toast.LENGTH_SHORT).show(); // Feedback to user
    }
}
