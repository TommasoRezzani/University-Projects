package com.example.verbose.model;

public class RepetitionInfo {
    private String id;
    private String name;
    private Category category;
    private User owner;

    public RepetitionInfo(String id, String name, Category category, User owner) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public User getOwnerInfo() {
        return owner;
    }
}
