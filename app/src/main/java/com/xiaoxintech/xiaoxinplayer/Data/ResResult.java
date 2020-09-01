package com.xiaoxintech.xiaoxinplayer.Data;

import java.util.ArrayList;

public class ResResult<T> {
    public int error_code;
    public String error_message;
    public ArrayList<T> file_infos;
}

