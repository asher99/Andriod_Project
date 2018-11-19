package com.example.myapplication.controller.controller.model.backend;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.myapplication.controller.controller.model.datasource.Firebase_DBManager;
import com.example.myapplication.controller.controller.model.entities.Ride;

public interface Backend {
    void addRide(final Ride Ride, final Firebase_DBManager.Action<Long> action);
}
