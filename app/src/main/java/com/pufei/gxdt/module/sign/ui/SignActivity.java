package com.pufei.gxdt.module.sign.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jaeger.library.StatusBarUtil;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.contents.EventBean;
import com.pufei.gxdt.module.sign.adapter.SignAdapter;
import com.pufei.gxdt.module.sign.model.GetScoreBean;
import com.pufei.gxdt.module.sign.model.SignEntity;
import com.pufei.gxdt.module.sign.model.SignInBean;
import com.pufei.gxdt.module.sign.utils.ResolutionUtil;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.SignUtils;
import com.pufei.gxdt.utils.UrlString;
import com.pufei.gxdt.widgets.MyFrontTextView;
import com.pufei.gxdt.widgets.SignView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private LinearLayout imageView;
    private TextView tvbang;
    private TextView tvTitle;
    List<String> timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.yellow), 0);
        setLightMode(this);
        initView();
        AppManager.getAppManager().addActivity(this);
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
        tvbang = (TextView) findViewById(R.id.tv_right);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imageView = (LinearLayout) findViewById(R.id.ll_title_left);
        tvSignDay = (TextView) findViewById(R.id.activity_main_tv_main_day);
        tvScore = (TextView) findViewById(R.id.activity_main_tv_score);
        tvYear = (TextView) findViewById(R.id.activity_main_tv_year);
        tvMonth = (TextView) findViewById(R.id.activity_main_tv_month);
        signView = (SignView) findViewById(R.id.activity_main_cv);
        btnSign = (AppCompatButton) findViewById(R.id.activity_main_btn_sign);
        tvTitle.setText("签到");
        tvbang.setText("查看榜单");
        tvbang.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
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
        //Random ran = new Random();
        for (int i = 1; i <= maxDate; i++) {
            SignEntity signEntity = new SignEntity();
            int Type = setVerfy(i);
            if (Type == 0) {
                signEntity.setDayType(0);
            } else if (Type == 1) {
                if (i == dayOfMonthToday) {
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
            if (timer >= getTimesMonthmorning()) {
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
            OkhttpUtils.post(UrlString.GETSCORE, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    final GetScoreBean getScoreBean = new Gson().fromJson(result, GetScoreBean.class);
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
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setLightMode(Activity activity) {
        setMIUIStatusBarDarkIcon(activity, true);
        setMeizuStatusBarDarkIcon(activity, true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
    }

    /**
     * 修改 MIUI V6  以上状态栏颜色
     */
    private static void setMIUIStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkIcon ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private static void setMeizuStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkIcon) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
