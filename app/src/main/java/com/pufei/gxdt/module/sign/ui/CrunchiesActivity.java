package com.pufei.gxdt.module.sign.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.pufei.gxdt.module.sign.adapter.CrunchinesAdapter;
import com.pufei.gxdt.module.sign.model.CrunchiesBean;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.UrlString;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wangwenzhang on 2017/8/15.
 */

public class CrunchiesActivity extends BasePictureActivity {
    private List<CrunchiesBean.ResultBean> list;
    private CrunchinesAdapter adapter;
    @Override
    public void initView() {
        addHeader();
        list=new ArrayList<>();
        adapter=new CrunchinesAdapter(this,list);
        //activityBasepictureTv.setText(getName());
        activityBasepictureBianji.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityBasepictureRc.setLayoutManager(layoutManager);
        activityBasepictureRc.setPullRefreshEnabled(false);
        activityBasepictureRc.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public String getName() {
        return "斗气榜";
    }

    @Override
    public void initData() {
        getData();
    }
    private void getData(){
        JSONObject jsonObject= KeyUtil.getJson(this);
        try {
            OkhttpUtils.post(UrlString.crunchies, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    CrunchiesBean resultBean=new Gson().fromJson(result,CrunchiesBean.class);
                    list.addAll(resultBean.getResult());
                    CrunchiesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Log.e("榜单的数据",result);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
