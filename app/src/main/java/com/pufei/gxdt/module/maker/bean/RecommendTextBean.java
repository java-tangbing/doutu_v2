package com.pufei.gxdt.module.maker.bean;

import java.io.Serializable;
import java.util.List;

public class RecommendTextBean implements Serializable {

    /**
     * code : 0
     * msg : success
     * result : ["我有个恋爱想和你谈一谈","我看您是欠电","我TN....一刀....","我的钱怎么没啦","我有个一个不成熟的小建议","我有个大胆的想法","我们的产品经理很屌"]
     */

    private String code;
    private String msg;
    private List<String> result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
