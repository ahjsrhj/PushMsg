package cn.imrhj.pushmsg.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.marshalchen.ultimaterecyclerview.DragDropTouchListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;


import butterknife.Bind;
import butterknife.ButterKnife;
import cn.imrhj.pushmsg.Adapter.DataListRecyclerViewAdapter;
import cn.imrhj.pushmsg.Callback.TouchHelperCallback;
import cn.imrhj.pushmsg.DataLoader.DataLoader;
import cn.imrhj.pushmsg.R;
import cn.imrhj.pushmsg.Utils.ListData;
import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;

public class AllRecordFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "AllRecordFragment";
    @Bind(R.id.base_swipe_list) UltimateRecyclerView mRecyclerView;
    @Bind(R.id.fab) FloatingActionButton mFabutton;
    private DataListRecyclerViewAdapter mAdapter;

//    private List<ListData> mDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_record, container, false);
        ButterKnife.bind(this, view);

//        addData();
        initRecycleView();
        return view;
    }


    /**
     * 初始化recycleView
     */
    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DataListRecyclerViewAdapter(getContext());
        DataLoader.getInstance().bindAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindRecycleView(mRecyclerView.mRecyclerView);
        mRecyclerView.setDefaultOnRefreshListener(this);
        ItemTouchHelper helper = new ItemTouchHelper(new TouchHelperCallback(mAdapter));
        helper.attachToRecyclerView(mRecyclerView.mRecyclerView);
        mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView.mRecyclerView));

        mFabutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入");
        View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.alert_dialog_input_box, null);
        final EditText inputEt = (EditText) dialogView.findViewById(R.id.art_input_box);
        builder.setView(dialogView);
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = inputEt.getText().toString();
                DataLoader.getInstance().addData(new ListData(data));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 下拉刷新回调
     */
    @Override
    public void onRefresh() {
        mAdapter.refresh();
        mRecyclerView.setRefreshing(false);
    }
}

