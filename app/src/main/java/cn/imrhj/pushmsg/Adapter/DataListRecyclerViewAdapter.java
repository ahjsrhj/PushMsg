package cn.imrhj.pushmsg.Adapter;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.GetCallback;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import cn.imrhj.pushmsg.DataLoader.DataLoader;
import cn.imrhj.pushmsg.R;
import cn.imrhj.pushmsg.Utils.CopyData;
import cn.imrhj.pushmsg.Utils.ListData;
import cn.imrhj.pushmsg.Utils.qToast;

/**
 * Created by rhj on 16/3/14.
 */
public class DataListRecyclerViewAdapter extends UltimateViewAdapter<UltimateRecyclerviewViewHolder> {

    private static final String TAG = "DataAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ListData> mDataList;
    private RecyclerView mRecycleView;

    public DataListRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        mDataList = DataLoader.getInstance().getmDataList();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public UltimateRecyclerviewViewHolder getViewHolder(View view) {
        return null;
    }

    /**
     * 渲染具体的ViewHolder
     * @param parent ViewHolder的容器
     * @return
     */
    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        return new NormalItemHolder((mLayoutInflater.inflate(R.layout.fragment_base_swipe_list, parent, false)));
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    /**
     * 绑定ViewHolder的数据
     * @param holder viewHolder
     * @param position 数据源list下标
     */
    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder holder, int position) {
        ListData entity = mDataList.get(position);

        if (entity == null) {
            return;
        }
        NormalItemHolder h = (NormalItemHolder) holder;
        h.mData.setText(entity.getData());
        h.mTime.setText(entity.getTime());
        if (entity.isStar()) {
            h.mImageView.setImageResource(R.drawable.ic_star);
        } else {
            h.mImageView.setImageResource(R.drawable.ic_star_border);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new NormalItemHolder(
                mLayoutInflater.inflate(R.layout.fragment_base_swipe_list, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public class NormalItemHolder extends UltimateRecyclerviewViewHolder{
        public TextView mData;
        public TextView mTime;
        public ImageView mImageView;

        public NormalItemHolder(View itemView) {
            super(itemView);
            mData = (TextView) itemView.findViewById(R.id.base_swipe_item_data);
            mTime = (TextView) itemView.findViewById(R.id.base_swipe_item_time);
            mImageView = (ImageView) itemView.findViewById(R.id.base_swipe_item_star);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onImageViewClick: ");

                }
            });

            itemView.findViewById(R.id.base_swipe_item_container)
                    .setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            CopyData.getInstance().copyData(mData.getText().toString());
                            qToast.s(mContext, "复制成功!");
                            return true;
                        }
                    });
        }
    }

    public void refresh() {
        DataLoader.getInstance().refresh();
    }
//
    public void addData(ListData data) {
        mDataList.add(0, data);
        notifyItemInserted(0);
        mRecycleView.scrollToPosition(0);

    }
    /**
     * 删除指定位置的view
     * @param position
     */
    public void onItemDismiss(final int position) {
        //删除操作暂时写在这里,之后应抽出来单独处理
        AVQuery<AVObject> query = new AVQuery<>("Post");
        query.getInBackground(mDataList.get(position).getObjectId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                if (e == null) {
                    object.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                mDataList.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                Log.e(TAG, "done: delete in background " + e.toString());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "done:query get in background " + e.toString());
                }
            }
        });
    }

    public void bindRecycleView(RecyclerView recyclerView) {
        this.mRecycleView = recyclerView;
    }
}

