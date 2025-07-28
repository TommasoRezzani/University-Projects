package com.example.verbose;

public final class Constants {
    public static final String SERVER_BASE_URL = "http://alexdubini.ddns.net:8000";
    public static final String VERBOSE_API_BASE_URL = SERVER_BASE_URL + "/verbose/";
    public static final String DATABASE_NAME = "verbose_db";

    //Nome shared pref dell' applicazione
    public static final String LOCAL_STORAGE = "verbose_storage";
    public static final String NOTIFICATION_CHANNEL_ID = "1000";

    /**
     * Chiave shared pref a cui è associato un long che rappresenta l'istante
     * in cui è stato effettuato l'ultimo update delle cateogorie
     */
    public static final String LAST_CATEGORIES_UPDATE = "last_categories_update";

    //Tempo di refresh in ms, equivalente a 24h
    public static final long CATEGORIES_REFRESH_TIME = 60 * 1000 * 60 * 24;

    public static final String NOTIFICATIONS_SETTINGS = "notifications_enabled";
}
