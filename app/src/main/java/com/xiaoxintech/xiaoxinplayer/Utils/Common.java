package com.xiaoxintech.xiaoxinplayer.Utils;

import android.content.Context;
import android.widget.Toast;

public class Common {
    private Toast toast;
    private static Common common;

    public static Common getInstance(){
        if (common == null) {
            common = new Common();
        }
        return common;
    }
    /**
     * 将toast封装起来，连续点击时可以覆盖上一个
     */
    public void ShowToast(String text, Context context){
        if(toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }
        toast.show();
    }
}
