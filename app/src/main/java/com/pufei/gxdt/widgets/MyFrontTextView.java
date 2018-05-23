package com.pufei.gxdt.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pufei.gxdt.app.App;

/**
 * Created by wangwenzhang on 2017/1/4.
 */
@SuppressLint("AppCompatCustomView")
public class MyFrontTextView extends TextView {
    public MyFrontTextView(Context context) {
        super(context);
        setTypeface();
    }

    public MyFrontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface();
    }

    public MyFrontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface();
    }
    private void setTypeface(){
        // 如果自定义typeface初始化失败，就用原生的typeface
        if(App.TEXT_TYPE == null){
            setTypeface(getTypeface()) ;
        }else{
            setTypeface(App.TEXT_TYPE) ;
        }
    }
}
