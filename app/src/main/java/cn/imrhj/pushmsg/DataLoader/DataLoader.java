package cn.imrhj.pushmsg.DataLoader;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cn.imrhj.pushmsg.Adapter.DataListRecyclerViewAdapter;
import cn.imrhj.pushmsg.Utils.ListData;

/**
 * Created by rhj on 16/3/17.
 * data loader class
 * use the singleton mode
 */
public class DataLoader {
    private static final String TAG = "DataLoader";
    private static DataLoader mInstance = null;
    private DataListRecyclerViewAdapter mAdapter = null;

    private List<ListData> mDataList;

    private DataCache mDataCache = new NetDataCache();

    private DataLoader() {
        mDataList = new ArrayList<>();
    }

    public static DataLoader getInstance() {
        if (mInstance == null) {
            mInstance = new DataLoader();
        }

        return mInstance;
    }

    public List<ListData> getmDataList() {
        if (mDataList.isEmpty()) {
            refresh();
        }
        return mDataList;
    }

    public void addData(final ListData data) {
        if (data != null) {
            final AVObject object = data.getAVObject();
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        data.setObjectId(object.getObjectId());
                        data.setTime(object.getCreatedAt());
                        mAdapter.addData(data);
                    }
                }
            });

        }
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        AVUser user = AVUser.getCurrentUser();
        if (user == null) {
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>("Post");
        query.whereEqualTo("pubUser", AVUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mDataList.clear();
                    for (int i = list.size() - 1; i >= 0; i--) {
                        mDataList.add(ListData.get(list.get(i)));
                    }
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }



    public void bindAdapter(DataListRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }
}
