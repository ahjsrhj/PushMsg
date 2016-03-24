package cn.imrhj.pushmsg.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import cn.imrhj.pushmsg.R;
import cn.imrhj.pushmsg.Utils.qToast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final String ERR_NO_NET = "未连接网络!";


    public static final String MATCHES_EMAIL = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])" +
            "+\\.)+([a-zA-Z0-9]{2,4})";

    private AlertDialog.Builder mSignInBuilder;
    private AlertDialog.Builder mRegisterBuilder;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    /**
     * 点击登录按钮回调函数
     * @param view
     */
    public void onSignInClick(View view) {
        mSignInBuilder = new AlertDialog.Builder(this);
        mSignInBuilder.setTitle("登录");
        View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog_sign_in, null);
        //获取edittext实例
        final EditText emailEt = (EditText) dialogView.findViewById(R.id.sign_in_email);
        final EditText passwordEt = (EditText) dialogView.findViewById(R.id.sign_in_password);

        mSignInBuilder.setView(dialogView);
        mSignInBuilder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userEmail = emailEt.getText().toString();
                String userPass = passwordEt.getText().toString();
                if (!userEmail.matches(MATCHES_EMAIL)) {
                    Toast.makeText(LoginActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    doSignIn(userEmail, userPass);
                }

            }
        });
        mSignInBuilder.show();
    }

    /**
     * 处理登录逻辑
     * @param uid
     * @param password
     */
    private void doSignIn(String uid, String password) {
        AVUser.logInInBackground(uid, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    //登录成功
                    turnToMainActivity();
                } else {
                    Log.e(TAG, "done: " + e.toString() + "error code :" + e.getCode() + "message :" + e.getMessage());
                    if (e.getCode() == 0) {
                        qToast.s(LoginActivity.this, ERR_NO_NET);
                    } else {
                        qToast.s(LoginActivity.this, e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 跳转到主界面
     */
    private void turnToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 点击注册按钮回调函数
     * @param view
     */
    public void onRegister(View view) {
        mRegisterBuilder = new AlertDialog.Builder(this);
        mRegisterBuilder.setTitle("注册");
        View dialogView= getLayoutInflater().inflate(R.layout.alert_dialog_register, null);
        //获取edittext的实例
        final EditText emailEt = (EditText) dialogView.findViewById(R.id.register_email);
        final EditText passwordEt = (EditText) dialogView.findViewById(R.id.register_password);
        final EditText usernameEt = (EditText) dialogView.findViewById(R.id.register_user_name);

        mRegisterBuilder.setView(dialogView);
        mRegisterBuilder.setPositiveButton("完成注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: 注册");
                String userEmail = emailEt.getText().toString();
                String userPass = passwordEt.getText().toString();
                String userName = usernameEt.getText().toString();
                if (!userEmail.matches(MATCHES_EMAIL)) {
                    Toast.makeText(LoginActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                } else if (userPass.length() < 6) {
                    Toast.makeText(LoginActivity.this, "密码长度过短", Toast.LENGTH_SHORT).show();
                } else {
                    register(userEmail, userPass, userName);
                }
            }
        });
        mRegisterBuilder.show();
    }

    /**
     * 注册用户
     * @param uid 使用email作为用户的uid,这是唯一的
     * @param userPass
     * @param userName  用户名为显示的昵称,可重复
     */
    private void register(final String uid, String userPass, final String userName) {
        AVUser user = new AVUser();
        user.setUsername(uid);
        user.setPassword(userPass);
        user.put("name", userName);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    turnToMainActivity();
                } else {
                    Log.e(TAG, "register failed!" + e.toString());
                }
            }
        });

    }
}
