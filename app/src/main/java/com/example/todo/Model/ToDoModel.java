package com.example.todo.Model;
public class ToDoModel {
    private int status;
    private String id;
    private String task;




    private String firebaseId;




    public String getFirebaseId() {
        return firebaseId;
    }
    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }




    public String  getId() {
        return id;
    }




    public int getStatus() {
        return status;
    }




    public void setId(String id) {
        this.id = id;
    }




    public void setStatus(int status) {
        this.status = status;
    }




    public void setTask(String task) {
        this.task = task;
    }




    public String getTask() {
        return task;
    }
}
