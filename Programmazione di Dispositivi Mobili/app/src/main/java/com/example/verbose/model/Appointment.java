package com.example.verbose.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Optional;

public class Appointment {
    private String id;
    @SerializedName("repetition")
    private RepetitionInfo repetitionInfo;
    @SerializedName("client")
    private User client;
    private boolean closed;
    private Date date;
    @SerializedName("time_slot")
    private int timeSlot;
    private boolean accepted;
    private String request;
    private Review review;
    @SerializedName("conference_link")
    private String conferenceLink;

    public Appointment(RepetitionInfo repetitionInfo, String request, Date date, int timeSlot){
        this.id = null;
        this.repetitionInfo = repetitionInfo;
        this.client = null;
        this.closed = false;
        this.date = date;
        this.timeSlot = timeSlot;
        this.accepted = false;
        this.request = request;
    }

    public String getId() {
        return id;
    }

    public RepetitionInfo getRepetitionInfo() {
        return repetitionInfo;
    }

    public User getClientInfo() {
        return client;
    }

    public boolean isClosed() {
        return closed;
    }

    public Date getDate() {
        return date;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getRequest() {
        return request;
    }

    public Optional<Review> getReview() {
        return Optional.ofNullable(review);
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getConferenceLink() {
        return conferenceLink;
    }
}
