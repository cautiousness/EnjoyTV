package com.fuj.enjoytv.model.tv;

import com.fuj.enjoytv.activity.tv_play.TVProgram;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gang
 */
public class Playlist implements Serializable {
    private static final long serialVersionUID = 1527870762375034356L;

    public String day;
    public List<TVProgram> playlist;
}
