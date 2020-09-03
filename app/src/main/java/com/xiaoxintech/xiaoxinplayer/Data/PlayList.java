package com.xiaoxintech.xiaoxinplayer.Data;

public class PlayList {
    /**
     * 名字
     */
    private String name;
    /**
     * 图片id
     */
    private int imageId;

    public PlayList(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name_val){
        name = name_val;
    }

    public int getImageId() {
        return imageId;
    }
}
