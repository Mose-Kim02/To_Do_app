package com.example.todo;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.Model.ToDoModel;
import com.example.todo.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddNewTask.DialogCloseListener {
    private DataBaseHandler db;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;
    private DatabaseReference firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = new DataBaseHandler(this);
        db.openDatabase();

        // Initialize Firebase Database
        firebaseDB = FirebaseDatabase.getInstance().getReference("Tasks");

        taskList = new ArrayList<>();
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);

        // Fetch tasks from Firebase
        syncTasksFromFirebase();

        // Floating Button Click to Add Task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    // Method to sync Firebase tasks to SQLite
    private void syncTasksFromFirebase() {
        firebaseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskList.clear();  // Clear existing list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ToDoModel task = snapshot.getValue(ToDoModel.class);
                    if (task != null) {
                        taskList.add(task);
                    }
                }
                Collections.reverse(taskList);
                runOnUiThread(() -> tasksAdapter.setTasks(taskList)); // Update UI safely
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}
