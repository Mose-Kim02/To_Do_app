package com.example.todo.Adapter;




import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;




import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;




import com.example.todo.AddNewTask;
import com.example.todo.MainActivity;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.Utils.DataBaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




import java.util.ArrayList;
import java.util.List;




public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {




    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DataBaseHandler db; // Add database handler
    private DatabaseReference databaseReference;


    private String userId;




    // Updated Constructor
    public ToDoAdapter(DataBaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
        this.todoList = new ArrayList<>();


        // Get the current logged-in user's ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("tasks");
        }
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));




        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTaskStatus(String.valueOf(item.getId()), isChecked ? 1 : 0);
            }
        });
    }




    private boolean toBoolean(int n) {
        return n != 0;
    }




    @Override
    public int getItemCount() {
        return todoList.size();
    }




    public Context getContext() {
        return activity;
    }




    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = new ArrayList<>(todoList);
        notifyDataSetChanged();
    }




    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        if (userId != null) {
            databaseReference.child(item.getId()).removeValue();
        }
        todoList.remove(position);
        notifyItemRemoved(position);
    }




    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getId()); // Use String ID for Firebase
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }




    private void updateTaskStatus(String taskId, int status) {
        if (userId != null) {
            databaseReference.child(taskId).child("status").setValue(status);
        }
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;




        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
