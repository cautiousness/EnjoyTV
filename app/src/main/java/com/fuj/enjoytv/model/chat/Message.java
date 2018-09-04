package com.fuj.enjoytv.model.chat;

public class Message {
    public static final int TYPE_RECEIVE = 0;
    public static final int TYPE_SEND = 1;

    public int type;
    public String content;
    public String time;
    public String fromUser;
}
