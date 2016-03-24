package cn.imrhj.pushmsg.Utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by rhj on 16/3/18.
 */
public class CopyData {

    private static CopyData mCopyData;

    private ClipboardManager mManager;

    private CopyData() {
        mManager = (ClipboardManager) AppContext.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static CopyData getInstance() {
        if (mCopyData == null) {
            mCopyData = new CopyData();
        }
        return mCopyData;
    }

    public void copyData(String data) {
        mManager.setPrimaryClip(ClipData.newPlainText("text", data));
    }
}
