package com.pufei.gxdt.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by wangwenzhang on 2017/4/19.
 */
public class DialogUtil {
    private AlertDialog makeBuider;
    private AlertDialog builder;
    private static DialogUtil dialogUtil;
    public DialogUtil(){
    }
    public static DialogUtil getInstance(){
        if (dialogUtil == null)
        {
            synchronized (DialogUtil.class)
            {
                if (dialogUtil == null)
                {
                    dialogUtil = new DialogUtil();
                }
            }
        }
        return dialogUtil;
    }

    public void showVersionDialog(Activity activity){
        makeBuider = new AlertDialog.Builder(activity).create();
        if (!activity.isFinishing()) {
            makeBuider.show();
        }
        Window window = makeBuider.getWindow();
        window.setContentView(R.layout.setting_version);
        TextView know = window.findViewById(R.id.setting_version_know);
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeBuider.dismiss();
            }
        });
    }
    public void canceDialog(final Activity activity, final PushAgent mPushAgent) {
        builder = new AlertDialog.Builder(activity).create();
        builder.setCanceledOnTouchOutside(false);
        builder.show();
        Window window = builder.getWindow();
        window.setContentView(R.layout.cance_dialog);
        TextView tvkaitong = (TextView) window.findViewById(R.id.vip_dialog_kaitong);
        TextView tvcancel = (TextView) window.findViewById(R.id.vip_dialog_cancel);
        tvkaitong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPushAgent.deleteAlias(App.userBean.getUid(), "User", new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, String message) {

                    }
                });
                App.userBean=null;
                SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL,null);
                EventBus.getDefault().postSticky(new EventMsg(MsgType.LOGIN_OUT));
                builder.dismiss();
                activity.finish();
            }
        });
    }
}
