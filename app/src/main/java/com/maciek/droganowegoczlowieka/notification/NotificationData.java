package com.maciek.droganowegoczlowieka.notification;

/**
 * Created by Geezy on 10.08.2018.
 */

public class NotificationData {

    public static String TEXT = "notificationText";
    public static String TITLE = "notificationTitle";

    private String title;
    private String textMessage;
    private int id;

    public NotificationData(String title, String textMessage,int id) {
        this.title = title;
        this.textMessage = textMessage;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
