package com.example.myapplication.controller.controller.model.datasource;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Firebase_DBManager {

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

    static DatabaseReference StudentsRef;
    //static List<Student> studentList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       // StudentsRef = database.getReference("students");
       // studentList = new ArrayList<>();
    }
}
