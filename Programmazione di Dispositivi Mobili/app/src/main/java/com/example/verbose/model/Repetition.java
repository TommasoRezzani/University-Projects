package com.example.verbose.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Repetition implements Serializable {
    private String id;
    private String name;
    @SerializedName("owner")
    private User owner;
    @SerializedName("start_time")
    private int startTime;
    @SerializedName("end_time")
    private int endTime;
    private Date date;
    private boolean closed;
    private Category category;
    private float vote;
    @SerializedName("reviews_cnt")
    private int reviewsCnt;

    @SerializedName("requests_cnt")
    private int requestsCnt;

    public Repetition(String name, int startTime, int endTime, Category category) {
        this.id = null;
        this.name = name;
        this.owner = null;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = null;
        this.closed = false;
        this.category = category;
        this.vote = 0.0f;
    }

    public Repetition(String id, String name, User owner, int startTime, int endTime, Date date, boolean open, Category category, float vote) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.closed = open;
        this.category = category;
        this.vote = vote;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public Date getDate() {
        return date;
    }

    public boolean isClosed() {
        return closed;
    }

    public float getVote() {
        return vote;
    }

    public int getReviewsCnt() {
        return reviewsCnt;
    }

    public int getRequestsCnt() {
        return requestsCnt;
    }

    public Category getCategory() {
        return category;
    }

    public RepetitionInfo getRepetitionInfo(){
        return new RepetitionInfo(
                id,
                name,
                category,
                owner
        );
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void close() {
        this.closed = true;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
