package com.example.verbose.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    String id;
    @SerializedName("appointment")
    String appointmentId;
    @SerializedName("author")
    User author;
    String content;
    float vote;

    public Review(Appointment appointment, String content, float vote){
        this(appointment.getId(), content, vote);
    }

    public Review(String appointmentId, String content, float vote){
        this.id = null;
        this.appointmentId = appointmentId;
        this.author = null;
        this.content = content;
        this.vote = vote;
    }

    public Review(String id, String appointmentId, User author, String content, float vote) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.author = author;
        this.content = content;
        this.vote = vote;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVote(float vote) {
        this.vote = vote;
    }

    public String getId() {
        return id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public User getAuthorInfo() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public float getVote() {
        return vote;
    }
}
