package com.pufei.gxdt.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.contents.EventBean;
import com.pufei.gxdt.module.home.adapter.SearchAdapter;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.UrlString;
import com.pufei.gxdt.widgets.MyFrontTextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tb on 2018/5/30.
 */

public class SearchActivity extends BaseMvpActivity  implements TextView.OnEditorActionListener{
    @BindView(R.id.tv_search_cancel)
    TextView tvSearchCancel;
    @BindView(R.id.rv_search)
    XRecyclerView rvSearch;
    @BindView(R.id.search_animimage)
    ImageView searchAnimimage;
    @BindView(R.id.search_et_input)
    EditText activitySearchSv;
    private String keyword;
    private List<String> list;
    private SearchAdapter adapter;
    private final String TAG=getClass().getName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String SHARE_TAG="SEARCH";
    private String SHARE_TYPE_TAG="history";
    private String history;
    private JSONArray jsonArray;
    private MyFrontTextView footTv;
    @Override
    public void setPresenter(BasePresenter presenter) {

    }
    @Override
    public void onStart() {
        if(!EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void initView() {
        list=new ArrayList<>();
        sharedPreferences = getSharedPreferences(SHARE_TAG,0);
        editor=sharedPreferences.edit();
        history=sharedPreferences.getString(SHARE_TYPE_TAG,"[]");
        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View footView = lif.inflate(R.layout.activity_search_item, null);
        footView.setLayoutParams(layoutParams);
        footTv= (MyFrontTextView) footView.findViewById(R.id.activity_search_tv);
        footTv.setTextColor(getResources().getColor(R.color.grey));
        try {
            jsonArray=new JSONArray(history);
            list=getList(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(layoutManager);
        rvSearch.setPullRefreshEnabled(false);
        adapter = new SearchAdapter(this, list);
        rvSearch.addFootView(footView);
        rvSearch.setAdapter(adapter);
        adapter.setOnItemClickListener(new SearchAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                try{
                    addBiao(list.get(postion));
                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        });
        activitySearchSv.setOnEditorActionListener(this);
        footTv.setTextSize(15);
        if (list.size()==0){
            footTv.setText("最近没有搜索哦");
        }else {
            footTv.setText("清除记录");
        }
        footTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                adapter.notifyDataSetChanged();
                footTv.setText("最近没有搜索哦");
                try {
                    jsonArray=new JSONArray("[]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.putString(SHARE_TYPE_TAG,"[]").apply();
            }
        });
    }
    private List<String> getList(JSONArray result){
        List<String> list=new ArrayList<>();
        if (result.length()>0){
            for (int i=0;i<result.length();i++){
                list.add(result.opt(i).toString());
            }
        }
        Collections.reverse(list);
        return list;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean eventBean) {
        if (eventBean.getContent().equals("search_detail")){
            list.clear();
            list.addAll(getList(jsonArray));
            if (footTv!=null){
                if (list.size()==0){
                    footTv.setText("最近没有搜索哦");
                }else {
                    footTv.setText("清除记录");
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
    @OnClick({ R.id.tv_search_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_cancel:
                if (activitySearchSv != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(activitySearchSv.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                }
                AppManager.getAppManager().finishActivity();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        editor.putString(SHARE_TYPE_TAG,jsonArray.toString()).apply();
        if (activitySearchSv != null) {
            // 得到输入管理对象
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                imm.hideSoftInputFromWindow(activitySearchSv.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
            }

        }
        if (EventBus.getDefault().isRegistered(this)){
            //加上判断
            EventBus.getDefault().unregister(this);
        }

        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction()==KeyEvent.KEYCODE_BACK){
            if (activitySearchSv != null) {
                // 得到输入管理对象
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                    imm.hideSoftInputFromWindow(activitySearchSv.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                }
            }
            AppManager.getAppManager().finishActivity();
        }
        editor.putString(SHARE_TYPE_TAG,jsonArray.toString()).apply();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            android.util.Log.e("dianjil",activitySearchSv.getText().toString());
            String result=activitySearchSv.getText().toString();
            if (result!=null){
                android.util.Log.e(TAG,result);
                jsonArray.put(result);
                try {
                    addBiao(result);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if (activitySearchSv != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(activitySearchSv.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    activitySearchSv.clearFocus();
                }

            }
            return true;
        }
        return false;
    }
    @Override
    public void getData() {

    }
    private void addBiao(final String keyword) throws JSONException {
        searchAnimimage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        searchAnimimage.startAnimation(animation);
        JSONObject jsonObject = KeyUtil.getJson(this);
        jsonObject.put("keyword", keyword);
        try {
            OkhttpUtils.post(UrlString.Search, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                        try {
                            SearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchAnimimage.clearAnimation();
                                    searchAnimimage.setVisibility(View.GONE);
                                    Intent intent = new Intent(SearchActivity.this, SearchDetailActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("search_detail", result);
                                    android.util.Log.e("searchActivity",keyword);
                                    bundle.putString("name",keyword);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }


}
