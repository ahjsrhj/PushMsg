package cn.imrhj.pushmsg.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.imrhj.pushmsg.Fragment.AllRecordFragment;
import cn.imrhj.pushmsg.Fragment.FavoriteFragment;
import cn.imrhj.pushmsg.R;
import cn.imrhj.pushmsg.Utils.qToast;

import static android.support.design.widget.NavigationView.*;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener {
    private static final String TAG = "MainActivity";

    private static final int PICK_FROM_FILE = 0;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;


    @Bind(R.id.drawer_layout) protected DrawerLayout mDrawer;
    @Bind(R.id.toolbar) protected Toolbar mToolbar;

    private Fragment mAllRecordFragment;
    private Fragment mFavoriteFragment;
    private NavigationView mNavigationView;

    AVUser mUser = AVUser.getCurrentUser();
    private ImageView avatarImageView;
    private File file;
    private AVFile avatarAVFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //add toolbar
        setSupportActionBar(mToolbar);


        //add DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

//        add navigationView
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_all);

        mAllRecordFragment = new AllRecordFragment();
        mFavoriteFragment = new FavoriteFragment();


        setDefaultFragment();
        addViewInfo();
    }

    /**
     * 添加用户信息
     */
    private void addViewInfo() {
        file = getExternalFilesDir("avatar.png");
        if (file == null) {
            file = new File(getExternalCacheDir() + "avatar.png");
        }

        if (mUser == null) return;
        View view = mNavigationView.getHeaderView(0);
        TextView username = (TextView) view.findViewById(R.id.nav_username);
        TextView email = (TextView) view.findViewById(R.id.nav_email);
        username.setText(mUser.getString("name"));
        email.setText(mUser.getUsername());
        //点击头像进行更换
        avatarImageView = (ImageView) view.findViewById(R.id.nav_avatar_round_iv);
        avatarImageView.setOnClickListener(this);
        if (mUser.get("avatar") != null) {
            String path = file.getAbsolutePath();
            Log.d(TAG, "addViewInfo: " + path);
            Bitmap avatarBitmap = null;

            if (!path.isEmpty()) {
                avatarBitmap = BitmapFactory.decodeFile(path);
            }
            if (avatarBitmap != null) {
                avatarImageView.setImageBitmap(avatarBitmap);
            } else {
                AVFile avatarFile = mUser.getAVFile("avatar");
                avatarFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (bytes.length != 0) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            saveBitmapToFile(bitmap);
                            avatarImageView.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        }

    }

    /**
     * 检测是否有用户登录
     */
    private void checkLogin() {

        if (mUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }
    }

    /**
     * 设置默认的fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, mAllRecordFragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle(R.string.all_record);

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_all:
                mToolbar.setTitle(R.string.all_record);
                transaction.replace(R.id.content, mAllRecordFragment);
                break;
            case R.id.nav_favorite:
                mToolbar.setTitle(R.string.my_favorite);
                transaction.replace(R.id.content, mFavoriteFragment);
                break;
            case R.id.nav_logout:
                AVUser.logOut();
                file.delete();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                Log.d(TAG, "onNavigationItemSelected: " + id);
        }

        //事务提交
        transaction.commit();
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nav_avatar_round_iv) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_FROM_FILE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_FILE:
                doCrop(data.getData());
                break;
            case PICK_FROM_CAMERA:
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                    setCropImg(data);
                }
                break;
        }
    }

    /**
     * @param data
     */
    private void setCropImg(Intent data) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            Bitmap bitmap = bundle.getParcelable("data");
            saveBitmapToFile(bitmap);
            saveAvatarFileToCloud(bitmap);
        }
    }

    /**
     * 保存头像到云端,并设置为用户头像
     * @param bitmap
     */
    private void saveAvatarFileToCloud(final Bitmap bitmap) {
        if (file != null && file.exists()) {
            try {
                final AVFile avatarFile = AVFile.withAbsoluteLocalPath("avatar.png", file.getAbsolutePath());
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("正在上传..");
                dialog.show();
                mUser.put("avatar", avatarFile);
                mUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            qToast.s(MainActivity.this, "设置成功");
                            avatarImageView.setImageBitmap(bitmap);
                        } else {
                            Log.e(TAG, "done: " + e.toString());
                            qToast.s(MainActivity.this, "上传头像失败!" + e.getMessage());
                        }
                        dialog.hide();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存头像到本地
     * @param bitmap
     */
    private void saveBitmapToFile(Bitmap bitmap) {
        if (file != null && file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "saveBitmapToFile: 保存成功!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doCrop(final Uri imgUri) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.setData(imgUri);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }
}
