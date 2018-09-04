package com.fuj.enjoytv.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class Chat implements Parcelable {
    public String pic;
    public String name;
    public String content;
    public String time;
    public int msgCount;

    public Chat() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pic);
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeInt(this.msgCount);
    }

    protected Chat(Parcel in) {
        this.pic = in.readString();
        this.name = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.msgCount = in.readInt();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
