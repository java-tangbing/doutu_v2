package com.pufei.gxdt.module.user.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventBean;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.sign.model.GetScoreBean;
import com.pufei.gxdt.module.sign.ui.CrunchiesActivity;
import com.pufei.gxdt.module.sign.ui.SignActivity;
import com.pufei.gxdt.module.user.activity.DraftActivity;
import com.pufei.gxdt.module.user.activity.FavoriteActivity;
import com.pufei.gxdt.module.user.activity.ProfileActivity;
import com.pufei.gxdt.module.user.activity.PublishActivity;
import com.pufei.gxdt.module.user.activity.SettingActivity;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.UrlString;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.pufei.gxdt.utils.SignUtils.IsToday;

public class UserFragment extends BaseFragment {

    @BindView(R.id.tv_checkin_state)
    TextView tv_checkin_state;
    @BindView(R.id.user_name)
    TextView tvUserName;
    @BindView(R.id.user_dec)
    TextView user_dec;
    @BindView(R.id.user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @BindView(R.id.fragment_collect_sign_tv)
    TextView fragmentCollectSign;
    @BindView(R.id.fragment_collect_loading_tv)
    TextView fragmentCollectLoadingTv;
    private String total = "0";
    private String times = "0";
    private boolean isSign = false;
    private List<String> timeList;
    private String SHARE_APP_USER = "USER";
    private SharedPreferences sharedPreferences;

    @Override
    public void initView() {
        initUserInfo();
    }

    @Override
    public void getData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initUserInfo() {
        timeList = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences(SHARE_APP_USER, 0);
        if (App.userBean != null) {
            tvUserName.setText(App.userBean.getName());
            if (!App.userBean.getHead().isEmpty()) {
                Glide.with(this).load(App.userBean.getHead()).into(ivUserHead);
            } else {
                Glide.with(this).load(R.mipmap.my_uer_picture).into(ivUserHead);
            }
        } else {
            tvUserName.setText("未登录");
            ivUserHead.setImageResource(R.mipmap.my_uer_picture);
        }
        initSign();
        getScore();
        initLoading();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user;
    }


    @OnClick({R.id.tv_checkin_state, R.id.fm_head, R.id.user_edit, R.id.tv_user_favorite, R.id.tv_user_publish, R.id.tv_about_product, R.id.tv_douqi, R.id.tv_user_draft})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_checkin_state:
                if (App.token != null) {
                    sharedPreferences.edit().putLong(App.token, System.currentTimeMillis()).apply();
                    initSign();
                    EventBus.getDefault().post(new EventBean(Contents.DAY_SIGN, null));
                    Intent intent1 = new Intent(getActivity(), SignActivity.class);
                    intent1.putExtra("total", total);
                    intent1.putExtra("times", times);
                    intent1.putExtra("issign", isSign);
                    startActivity(intent1);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.fm_head:
                if (App.userBean == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.user_edit:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.tv_user_publish:
                startActivity(new Intent(getActivity(), PublishActivity.class));
                break;
            case R.id.tv_user_favorite:
                startActivity(new Intent(getActivity(), FavoriteActivity.class));
                break;
            case R.id.tv_user_draft:
                startActivity(new Intent(getActivity(), DraftActivity.class));
                break;
            case R.id.tv_douqi:
                startActivity(new Intent(getActivity(), CrunchiesActivity.class));
                break;
            case R.id.tv_about_product:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

            default:
                break;
        }
    }

    private void initSign() {
        if (App.token != null) {
            long tiem = sharedPreferences.getLong(App.token, 0);
            boolean isSign = IsTodaySign(tiem);
            if (isSign) {
                fragmentCollectSign.setVisibility(View.GONE);
            } else {
                fragmentCollectSign.setVisibility(View.VISIBLE);
            }
        } else {
            fragmentCollectSign.setVisibility(View.GONE);
        }

    }

    private void initLoading() {
        if (App.token != null) {
            fragmentCollectLoadingTv.setVisibility(View.GONE);
        } else {
            fragmentCollectLoadingTv.setVisibility(View.VISIBLE);
        }
    }

    public boolean IsTodaySign(long day) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = new Date(day);
        cal.setTime(date);
        android.util.Log.e("两个时间", cal.get(Calendar.DAY_OF_YEAR) + "   " + pre.get(Calendar.DAY_OF_YEAR));
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventData(EventMsg type) {
        //   Log.e(TAG, "eventData");
        if (type.getTYPE() == MsgType.LOGIN_SUCCESS) {
            initUserInfo();
        } else if (type.getTYPE() == MsgType.LOGIN_OUT) {
            initUserInfo();
        } else if (type.getTYPE() == MsgType.UPDATA_USER) {
            initUserInfo();
            EventBus.getDefault().removeStickyEvent(type);//移除sticky事件
        }
    }

    private void getScore() {
        JSONObject jsonObject = KeyUtil.getJson(getActivity());
        try {
            jsonObject.put("auth", App.token);
            OkhttpUtils.post(UrlString.GETSCORE, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    try {
                        final GetScoreBean getScoreBean = new Gson().fromJson(result, GetScoreBean.class);
                        if (getScoreBean.getResult() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    times = getScoreBean.getResult().getTimes();
                                    total = getScoreBean.getResult().getTotal_score();
                                    if (getScoreBean.getResult().getDatastr() != null) {
                                        timeList = getScoreBean.getResult().getDatastr();
                                    }
                                    if (timeList.size() != 0) {
                                        String time = timeList.get(timeList.size() - 1);
                                        android.util.Log.e("最后一组时", System.currentTimeMillis() + "");
                                        long timer = Long.parseLong(time);
                                        isSign = IsToday(timer);
                                        try {
                                            if (isSign) {
                                                fragmentCollectSign.setVisibility(View.GONE);
//                                                fragmentCollectSignBtn.setText("已签");
                                            } else {
//                                                fragmentCollectSignBtn.setText("未签");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
//                                    try {
//                                        android.util.Log.e("StoreActivity", App.Total_score + "----");
//                                        App.Total_score = getScoreBean.getResult().getTotal_score();
//                                        EventBus.getDefault().post(new EventBean(Constants.USER_EXCHANGE, null));
//                                        fragmentCollectFenshu.setText(App.Total_score);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
                                }
                            });
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    android.util.Log.e("获取的积", result);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
