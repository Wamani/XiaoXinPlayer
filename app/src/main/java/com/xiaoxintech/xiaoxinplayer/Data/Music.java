package com.xiaoxintech.xiaoxinplayer.Data;

public class Music {
    private final String name;
    private final String author;
    private boolean isDir;
    private int mode;

    public Music(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }
}
