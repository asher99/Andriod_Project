package com.example.myapplication.controller.controller.model.backend;

import com.example.myapplication.controller.controller.model.datasource.Firebase_DBManager;

public class BackendFactory {
    private static final Backend ourInstance = new Firebase_DBManager();

    public static Backend getInstance() {
        return ourInstance;
    }

    private BackendFactory() {
    }
}
