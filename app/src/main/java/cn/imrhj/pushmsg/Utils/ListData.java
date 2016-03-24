package cn.imrhj.pushmsg.Utils;

import android.util.Log;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rhj on 16/3/14.
 * 数据类
 */
public class ListData {
    private String data;
    private Date time;
    private boolean isStar;
    private String objectId;

    public ListData(String data) {
        this.data = data;
        this.time = null;
        this.objectId = null;
        this.isStar = false;
    }

    public ListData(String data, Date time) {
        this.data = data;
        this.time = time;
        isStar = false;
        objectId = null;
    }

    public ListData(String data, Date time, String objectId) {
        this.data = data;
        this.time = time;
        this.objectId = objectId;
        this.isStar = false;
    }

    public ListData(String data, Date time, String objectId, boolean isStar) {
        this.data = data;
        this.time = time;
        this.objectId = objectId;
        this.isStar = isStar;
    }



    public String getData() {
        return data;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return DateFormat.getDateString(time);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }

    public static ListData get(AVObject object) {
        return  new ListData(object.getString("data"), object.getCreatedAt(), object.getObjectId());
    }

    public AVObject getAVObject() {
        AVObject object = new AVObject("Post");
        object.put("data", data);
        object.put("pubUser", AVUser.getCurrentUser().getUsername());
        return object;
    }

}
