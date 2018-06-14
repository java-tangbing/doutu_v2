package com.pufei.gxdt.module.user.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UrlString;
import com.pufei.gxdt.widgets.MyFrontTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wangwenzhang on 2017/4/1.
 */
public class FeedBackActivity extends BaseActivity {
    /* @InjectView(R.id.activity_feedback_return)
     LinearLayout activityFeedbackReturn;
     @InjectView(R.id.activity_feedback_ok)
     MyFrontTextView activityFeedbackOk;
     @InjectView(R.id.activity_feedback_respose)
     EditText activityFeedbackRespose;
     @InjectView(R.id.activity_feedback_qq)*/
    // EditText activityFeedbackQq;
    @BindView(R.id.activity_feedback_return)
    LinearLayout activityFeedbackReturn;
    @BindView(R.id.activity_feedback_ok)
    MyFrontTextView activityFeedbackOk;
    @BindView(R.id.activity_feedback_respose)
    EditText activityFeedbackRespose;
    @BindView(R.id.activity_feedback_qq)
    EditText activityFeedbackQq;
    private String content;
    private String qq;

    @Override
    public void initView() {

    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.acticity_feedback;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.activity_feedback_return, R.id.activity_feedback_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_feedback_return:
                finish();
                break;
            case R.id.activity_feedback_ok:
                try {
                    senddate();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private Boolean setdate() {
        content = activityFeedbackRespose.getText().toString();
        qq = activityFeedbackQq.getText().toString();
        if (TextUtils.isEmpty(content.replaceAll("\\s*", ""))) {
            ToastUtils.showShort(this, "请输入反馈意见");
        } else if (content.length() > 100) {
            ToastUtils.showShort(this, "超过了200字数限制");
            return false;
        }
        if (qq.length() <= 5 || qq.length() >= 13) {
            ToastUtils.showShort(this, "请输入正确的QQ号");
            return false;
        }
        return true;
    }

    private void senddate() throws JSONException, IOException {
        if (setdate()) {
            JSONObject jsonObject = KeyUtil.getJson(this);
            jsonObject.put("advice", content);
            jsonObject.put("qq", qq);
            OkhttpUtils.post(UrlString.Send, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    FeedBackActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(getApplicationContext(), "发送成功");
                            finish();
                        }
                    });

                }
            });
        }

    }
}
