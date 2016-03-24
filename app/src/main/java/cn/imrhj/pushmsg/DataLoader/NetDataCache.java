package cn.imrhj.pushmsg.DataLoader;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.imrhj.pushmsg.Utils.ListData;

/**
 * Created by rhj on 16/3/17.
 */
public class NetDataCache implements DataCache {
    private static final String TAG = "NetDataCache";
    private List<ListData> mDatas = null;
    private boolean flag = false;

    @Override
    public List<ListData> get() {
        if (mDatas == null) {
            getAVObject();
        }
        while (!flag);
        return mDatas;
    }

    private void getAVObject() {
        AVQuery<AVObject> query = new AVQuery<>("Post");
//        query.whereEqualTo("pubUser", AVUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.d(TAG, "done: find ok");
                    for (AVObject object : list) {
                        mDatas.add(ListData.get(object));
                    }
                } else {
                    Log.e(TAG, "done: " + e.toString());
                }
                flag = true;
            }
        });

//        try {
//            List<AVObject> list = query.find();
//            for (AVObject object : list) {
//                mDatas.add(ListData.get(object));
//            }
//        } catch (AVException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void put(List<ListData> datas) {

    }
}
