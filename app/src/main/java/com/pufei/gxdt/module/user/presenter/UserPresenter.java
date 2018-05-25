package com.pufei.gxdt.module.user.presenter;

import android.content.Context;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.user.bean.CheckinBean;
import com.pufei.gxdt.module.user.view.UserView;
import com.pufei.gxdt.utils.SharedPreferencesUtil;

import io.reactivex.functions.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 创建者： wangwenzhang 时间： 2018/3/7.
 */

public class UserPresenter extends BasePresenter<UserView> {
    private Context context;

    public UserPresenter(Context context) {
        this.context = context;
    }

    public void getStatistical(int subject) {
        int wrong = 0;
//            WrongDao wrongDao=new WrongDao(context);
//            List<WrongBankBean> list=wrongDao.getDatabse();
//
//            if(subject == 1) {
//                for (int i=0;i<list.size();i++){
//                    if (list.get(i).getSubject()==1){
//                        wrong++;
//                    }
//                }
//            }else {
//                for (int i=0;i<list.size();i++){
//                    if (list.get(i).getSubject() != 1){
//                        wrong++;
//                    }
//                }
//            }

        baseview.wrongStatistical(wrong);
    }

    public void getAverage(int subject) {
        int average = 0;
//            int number = 0;
//            int total = 0;
//            ResultsDao resultsDao=new ResultsDao(context);
//            List<ResultsBean> list=resultsDao.getDatabse();
//
//            if(subject == 1) {
//                for (int i=0;i<list.size();i++){
//                    if (list.get(i).getKemu()==1){
//                        number++;
//                        total=list.get(i).getScore();
//                    }
//                }
//                if (number>0){
//                    average=total/number;
//                }
//            }else {
//                for (int i=0;i<list.size();i++){
//                    if (list.get(i).getKemu() !=1){
//                        number++;
//                        total=list.get(i).getScore();
//                    }
//                }
//                if (number>0){
//                    average=total/number;
//                }
//            }

        baseview.examAverage(average);
    }

    public void getAnserTotal(int subject) {
        if (subject == 1) {
            int first = SharedPreferencesUtil.getInstance().getInt(Contents.USER_ANSER_TOTAL_FRIST, 0);
            baseview.answerTotal(first);
        } else {
            int four = SharedPreferencesUtil.getInstance().getInt(Contents.USER_ANSER_TOTAL_fOUR, 0);
            baseview.answerTotal(four);
        }
    }

//    public void checkinState(RequestBody body) {
//        Disposable disposable = ApiService.setPersonal().isCheckin(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<CheckinBean>() {
//                    @Override
//                    public void accept(CheckinBean result) throws Exception {
//                        baseview.showCheckinState(result);
//                    }
//                });
//        addSubscription(disposable);
//    }
}
