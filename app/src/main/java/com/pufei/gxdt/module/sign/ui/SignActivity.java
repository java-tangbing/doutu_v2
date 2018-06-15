package com.pufei.gxdt.module.sign.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventBean;
import com.pufei.gxdt.module.sign.model.GetScoreBean;
import com.pufei.gxdt.module.sign.model.SignInBean;
import com.pufei.gxdt.module.sign.utils.ResolutionUtil;
import com.pufei.gxdt.module.sign.model.SignEntity;
import com.pufei.gxdt.module.sign.adapter.SignAdapter;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.SignUtils;
import com.pufei.gxdt.utils.UrlString;
import com.pufei.gxdt.widgets.MyFrontTextView;
import com.pufei.gxdt.widgets.SignView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignActivity extends AppCompatActivity {
    private TextView tvSignDay;
    private TextView tvScore;
    private TextView tvYear;
    private TextView tvMonth;
    private SignView signView;
    private AppCompatButton btnSign;
    private List<SignEntity> data;
    private ImageView imageView;
    private MyFrontTextView tvbang;
    List<String> timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
        onReady();
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

    @SuppressLint("RestrictedApi")
    private void initView() {
        timeList = new ArrayList<>();
        Intent intent = getIntent();
        //Bundle bundle=intent.getExtras();
        //getScore();
        tvbang = (MyFrontTextView) findViewById(R.id.activity_sign_bangdan);
        imageView = (ImageView) findViewById(R.id.activity_sign_cance);
        tvSignDay = (TextView) findViewById(R.id.activity_main_tv_main_day);
        tvScore = (TextView) findViewById(R.id.activity_main_tv_score);
        tvYear = (TextView) findViewById(R.id.activity_main_tv_year);
        tvMonth = (TextView) findViewById(R.id.activity_main_tv_month);
        signView = (SignView) findViewById(R.id.activity_main_cv);
        btnSign = (AppCompatButton) findViewById(R.id.activity_main_btn_sign);
        if (signView != null) {
            signView.setOnTodayClickListener(onTodayClickListener);
        }
        if (btnSign != null) {
            //noinspection deprecation
            btnSign.setSupportBackgroundTintList(getResources().getColorStateList(R.color.color_user_button_submit));
            btnSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        }
        //Log.e("传过来的boolean",intent.getBooleanExtra("issign",false)+"");
        boolean isSign = intent.getBooleanExtra("issign", false);
        if (isSign) {
            btnSign.setEnabled(false);
            btnSign.setText(R.string.have_signed);
        }
        tvSignDay.setText(Html.fromHtml(String.format(getString(R.string.you_have_sign), "#999999", "#1B89CD", Integer.valueOf(intent.getStringExtra("times")))));
        tvScore.setText(intent.getStringExtra("total"));
        //---------------------------------分辨率适配----------------------------------
        ResolutionUtil resolutionUtil = ResolutionUtil.getInstance();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = resolutionUtil.formatVertical(40);
        tvSignDay.setLayoutParams(layoutParams);
        tvSignDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(42));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = resolutionUtil.formatVertical(40);
        tvScore.setLayoutParams(layoutParams);
        tvScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(95));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(130));
        layoutParams.topMargin = resolutionUtil.formatVertical(54);
        View llDate = findViewById(R.id.activity_main_ll_date);
        if (llDate != null) {
            llDate.setLayoutParams(layoutParams);
        }
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = resolutionUtil.formatHorizontal(43);
        tvYear.setLayoutParams(layoutParams);
        tvYear.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(43));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = resolutionUtil.formatHorizontal(44);
        tvMonth.setLayoutParams(layoutParams);
        tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(43));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(818));
        signView.setLayoutParams(layoutParams);
        getScore();
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(142));
        layoutParams.topMargin = resolutionUtil.formatVertical(111);
        layoutParams.leftMargin = layoutParams.rightMargin = resolutionUtil.formatHorizontal(42);
        if (btnSign != null) {
            btnSign.setLayoutParams(layoutParams);
            btnSign.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(54));
        }
        tvbang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignActivity.this, CrunchiesActivity.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().finishActivity();
            }
        });
    }

    private int getDay(String time) {
        if (time != null) {
            long timer = Long.parseLong(time) * 1000;
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(timer);
            pre.setTime(predate);
            return pre.get(Calendar.DAY_OF_MONTH);
        }
        return 0;
    }

    private int dayOfMonthToday;

    private void onReady() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvMonth.setText(getResources().getStringArray(R.array.month_array)[month]);
        Calendar calendarToday = Calendar.getInstance();
        int maxDate = calendarToday.get(Calendar.DATE);
        dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
        data = new ArrayList<>();
        Log.e("本月天数：", maxDate + "" + "当天多少号" + dayOfMonthToday);
        //Random ran = new Random();
        for (int i = 1; i <= maxDate; i++) {
            SignEntity signEntity = new SignEntity();
            int Type = setVerfy(i);
            if (Type == 0) {
                signEntity.setDayType(0);
            } else if (Type == 1) {
                Log.e("循环到了：", i + "" + "当天多少号" + dayOfMonthToday);
                if (i == dayOfMonthToday) {
                    Log.e("到这啦", "--------------------");
                    signEntity.setDayType(2);
                } else {
                    signEntity.setDayType(1);
                }

            } else {
                signEntity.setDayType(3);
            }
            data.add(signEntity);
        }
        SignAdapter signAdapter = new SignAdapter(data);
        signView.setAdapter(signAdapter);
    }

    private int setVerfy(int day) {
        int type = 1;
        for (int a = 0; a < timeList.size(); a++) {
            long timer = Long.parseLong(timeList.get(a)) * 1000;
            Log.e("SignAcitivty", "签到时间：" + timer + "---月初时间：" + getTimesMonthmorning() + "a" + a);
            if (timer >= getTimesMonthmorning()) {
                Log.e("SignAcitivty", "当天日期：" + day + "---签到日期：" + getDay(timeList.get(a)));
                if (day == getDay(timeList.get(a))) {
                    type = 0;
                }
            } else {
                type = 4;
            }
        }
        return type;
    }

    public static long getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return (cal.getTimeInMillis());
    }

    private boolean isMoth(String time) {
        long timer = Long.parseLong(time) * 1000;
        Calendar calendar = Calendar.getInstance();
        int now = calendar.get(Calendar.MONTH) + 1;
        Date predate = new Date(timer);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(predate);
        int old = calendar1.get(Calendar.MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        String date = dateFormat.format(predate);
        Log.e("SignActivity", "当前月:" + now + "显示月：" + old + "日期" + date);
        if (old == now) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDang(int day) {
        for (int i = 0; i < timeList.size(); i++) {
            if (isMoth(timeList.get(i))) {
                if (day == getDay(timeList.get(i))) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    private void onSign(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SignDialogFragment signDialogFragment = SignDialogFragment.newInstance(i);
        signDialogFragment.setOnConfirmListener(onConfirmListener);
        signDialogFragment.show(fragmentManager, SignDialogFragment.TAG);
    }

    private void signToday() {
        data.get(signView.getDayOfMonthToday() - 1).setDayType(SignView.DayType.SIGNED.getValue());
        signView.notifyDataSetChanged();
        btnSign.setEnabled(false);
        btnSign.setText(R.string.have_signed);
    }

    private SignView.OnTodayClickListener onTodayClickListener = new SignView.OnTodayClickListener() {
        @Override
        public void onTodayClick() {
            signIn();
        }
    };

    private SignDialogFragment.OnConfirmListener onConfirmListener = new SignDialogFragment.OnConfirmListener() {
        @Override
        public void onConfirm() {
            signToday();
        }
    };

    private void signIn() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("auth", App.userBean.getAuth());
            jsonObject.put("type", "1");
            OkhttpUtils.post(UrlString.SIGN_IN, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    try {
                        final SignInBean signInBean = new Gson().fromJson(result, SignInBean.class);
                        if (signInBean.getResult().getScore() != null) {
                            try {
                                String score = signInBean.getResult().getScore();
                                onSign(Integer.valueOf(score));
                                SignUtils.getInstance(SignActivity.this).setTime(System.currentTimeMillis());
                                SignActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvSignDay.setText(Html.fromHtml(String.format(getString(R.string.you_have_sign), "#999999", "#1B89CD", Integer.valueOf(signInBean.getResult().getTimes()))));
                                        App.Total_score = signInBean.getResult().getTotal_score();
                                        tvScore.setText(App.Total_score);
                                        EventBus.getDefault().post(new EventBean("Sign", null));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JsonSyntaxException | NumberFormatException e) {
                        e.printStackTrace();
                    }
                    Log.e("签到的结果", result);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getScore() {
        timeList.clear();
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("auth", App.userBean.getAuth());
            OkhttpUtils.post(UrlString.GETSCORE, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    final GetScoreBean getScoreBean = new Gson().fromJson(result, GetScoreBean.class);
                    //Log.e("签到的列表",getScoreBean.getResult().getDatastr().size()+"");
                    if (getScoreBean.getResult() != null && getScoreBean.getResult().getDatastr() != null) {
                        SignActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //timeList.add("1501817082");
                                try {
                                    timeList.addAll(getScoreBean.getResult().getDatastr());
                                    onReady();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    Log.e("获取的积分", result);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
