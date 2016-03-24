package cn.imrhj.pushmsg;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import cn.imrhj.pushmsg.Utils.AppContext;

/**
 * Created by rhj on 16/3/13.
 */
public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        AVOSCloud.initialize(this, "Y31F8rvXojyIE3WbPHz9MBU4-gzGzoHsz", "w4FQS8KiqC5gPTD0CIT7nKCB");

        AppContext.setContext(getApplicationContext());
    }
}
