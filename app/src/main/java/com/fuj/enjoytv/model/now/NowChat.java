package com.fuj.enjoytv.model.now;

/**
 * Created by gang
 */
public class NowChat {
    public NowChat(String user, String content, int level) {
        this.user = user;
        this.content = content;
        this.level = level;
    }

    public String user;
    public String content;
    public int level;
}
