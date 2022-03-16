package com.ogif.kotae.data.model;

import androidx.annotation.NonNull;

public class Grade {
    private String id, name;

    public Grade(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String toString() {
        return name;
    }
}
