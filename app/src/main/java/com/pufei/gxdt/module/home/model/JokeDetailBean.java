package com.pufei.gxdt.module.joke.model;

import java.util.List;

/**
 * Created by wangwenzhang on 2017/2/24.
 */
public class JokeDetailBean {

    /**
     * code : 0
     * msg : success
     * result : {"id":"337","jid":"673","content":["有个很有钱的富豪，一天回家，进屋的时候发现他的爱犬被人杀了，他痛苦流涕，大声哭喊。这时，一个蒙面男子说话了：有人花大价钱让我取你狗命！","以前，姑姑老是说附近小卖部的老板流里流气，一看就不是好人。结果她女儿听多了，去留意了一下，一看发现是自己最喜欢的朋克风，果断倒追\u2026\u2026现在，姑姑不停嘀咕:\u201c唉，这就是命。\u201d","爆笑GIF：感觉好尴尬啊","1487904685.73.gif","爆笑GIF：老王为了给媳妇吹空调也是拼了","1487904697.61.jpg","爆笑GIF：大兄弟胃口不错哦","1487904697.79.jpg","爆笑GIF：这样小偷肯定偷不走了吧","1487904698.15.jpg","爆笑GIF：听说白银可以测试毒，这个也有这功能吗","1487904698.52.gif","爆笑GIF：老板这广告好霸气","1487904699.35.jpg","爆笑GIF：卖家和买家在逗我们玩吗","1487904699.81.jpg","1487904700.27.jpg"],"dateline":"1487904701"}
     */

    private String code;
    private String msg;
    /**
     * id : 337
     * jid : 673
     * content : ["有个很有钱的富豪，一天回家，进屋的时候发现他的爱犬被人杀了，他痛苦流涕，大声哭喊。这时，一个蒙面男子说话了：有人花大价钱让我取你狗命！","以前，姑姑老是说附近小卖部的老板流里流气，一看就不是好人。结果她女儿听多了，去留意了一下，一看发现是自己最喜欢的朋克风，果断倒追\u2026\u2026现在，姑姑不停嘀咕:\u201c唉，这就是命。\u201d","爆笑GIF：感觉好尴尬啊","1487904685.73.gif","爆笑GIF：老王为了给媳妇吹空调也是拼了","1487904697.61.jpg","爆笑GIF：大兄弟胃口不错哦","1487904697.79.jpg","爆笑GIF：这样小偷肯定偷不走了吧","1487904698.15.jpg","爆笑GIF：听说白银可以测试毒，这个也有这功能吗","1487904698.52.gif","爆笑GIF：老板这广告好霸气","1487904699.35.jpg","爆笑GIF：卖家和买家在逗我们玩吗","1487904699.81.jpg","1487904700.27.jpg"]
     * dateline : 1487904701
     */

    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private String id;
        private String jid;
        private String dateline;
        private List<String> content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJid() {
            return jid;
        }

        public void setJid(String jid) {
            this.jid = jid;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }
    }
}
