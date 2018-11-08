package com.example.myapplication.controller.controller.model.backend;

public class BackendFactory {
    private static final BackendFactory ourInstance = new BackendFactory();

    public static BackendFactory getInstance() {
        return ourInstance;
    }

    private BackendFactory() {
    }   
}
