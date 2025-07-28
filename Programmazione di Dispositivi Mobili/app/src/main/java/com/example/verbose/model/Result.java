package com.example.verbose.model;

//TODO remove caller before release

public class Result<T> {
    private final EventType eventType;
    private T data;

    private final String callerName;
    private final Status status;

    boolean handled;
    Throwable throwable;

    public Result(T data, EventType eventType, String callerName){
        this.data = data;
        this.eventType = eventType;
        this.callerName = callerName;
        this.status = Status.SUCCESS;
        this.throwable = null;

        this.handled = false;
    }

    public Result(Throwable throwable, EventType eventType, String callerName){
        this.throwable = throwable;
        this.eventType = eventType;
        this.callerName = callerName;
        this.status = Status.FAILURE;

        this.handled = false;
    }

    public T getData() {
        return data;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void handle() {
        this.handled = true;
    }

    public boolean isHandled() {
        return handled;
    }

    public String getCallerName() {
        return callerName;
    }

    public boolean isSuccess(){
        return status == Status.SUCCESS;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public enum Status{
        SUCCESS,
        FAILURE
    }

    public enum EventType{
        SIGN_UP,
        SIGN_IN,
        LOGOUT,
        GET_USER,
        UPDATE_USER,
        GET_REPETITIONS,
        ADD_REPETITION,
        GET_APPOINTMENTS,
        SENT_APPOINTMENT,
        GET_NOTIFICATIONS,
        GET_REQUESTS,
        GET_FAVORITES,
        GET_REVIEWS,
        GET_CATEGORIES,
        GET_SLOTS,
        CHECK_USERNAME, POST_REVIEW
    }
}
