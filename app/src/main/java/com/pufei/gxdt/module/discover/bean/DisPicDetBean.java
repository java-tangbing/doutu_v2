package com.pufei.gxdt.module.discover.bean;

import java.util.List;

public class DisPicDetBean  {
    /**
     * code : 0
     * msg : success
     * result : {"id":"4","orginid":"30","orgintable":"amuse_images1","uid":"","url":"","hot":"","view":"","count":"1","data":[{"did":"4","index":"0","centerX":"149568","centerY":"334","width":"20","height":"20","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/5697c39b.jpg","textName":"20","textFontSize":"20","textFontColor":"20","textFont":"20","zoom":"","rolling":""},{"did":"4","index":"1","centerX":"168","centerY":"333","width":"20","height":"20","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/3ce8774b.jpg","textName":"20","textFontSize":"20","textFontColor":"20","textFont":"20","zoom":"","rolling":""}]}
     */

    private String code;
    private String msg;
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
        /**
         * id : 4
         * orginid : 30
         * orgintable : amuse_images1
         * uid :
         * url :
         * hot :
         * view :
         * count : 1
         * data : [{"did":"4","index":"0","centerX":"149568","centerY":"334","width":"20","height":"20","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/5697c39b.jpg","textName":"20","textFontSize":"20","textFontColor":"20","textFont":"20","zoom":"","rolling":""},{"did":"4","index":"1","centerX":"168","centerY":"333","width":"20","height":"20","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/3ce8774b.jpg","textName":"20","textFontSize":"20","textFontColor":"20","textFont":"20","zoom":"","rolling":""}]
         */

        private String id;
        private String orginid;
        private String orgintable;
        private String uid;
        private String url;
        private String hot;
        private String view;
        private String count;
        private List<DataBean> data;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrginid() {
            return orginid;
        }

        public void setOrginid(String orginid) {
            this.orginid = orginid;
        }

        public String getOrgintable() {
            return orgintable;
        }

        public void setOrgintable(String orgintable) {
            this.orgintable = orgintable;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * did : 4
             * index : 0
             * centerX : 149568
             * centerY : 334
             * width : 20
             * height : 20
             * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/5697c39b.jpg
             * textName : 20
             * textFontSize : 20
             * textFontColor : 20
             * textFont : 20
             * zoom :
             * rolling :
             */

            private String did;
            private String index;
            private String centerX;
            private String centerY;
            private String width;
            private String height;
            private String url;
            private String textName;
            private String textFontSize;
            private String textFontColor;
            private String textFont;
            private String zoom;
            private String rolling;

            public String getDid() {
                return did;
            }

            public void setDid(String did) {
                this.did = did;
            }

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public String getCenterX() {
                return centerX;
            }

            public void setCenterX(String centerX) {
                this.centerX = centerX;
            }

            public String getCenterY() {
                return centerY;
            }

            public void setCenterY(String centerY) {
                this.centerY = centerY;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTextName() {
                return textName;
            }

            public void setTextName(String textName) {
                this.textName = textName;
            }

            public String getTextFontSize() {
                return textFontSize;
            }

            public void setTextFontSize(String textFontSize) {
                this.textFontSize = textFontSize;
            }

            public String getTextFontColor() {
                return textFontColor;
            }

            public void setTextFontColor(String textFontColor) {
                this.textFontColor = textFontColor;
            }

            public String getTextFont() {
                return textFont;
            }

            public void setTextFont(String textFont) {
                this.textFont = textFont;
            }

            public String getZoom() {
                return zoom;
            }

            public void setZoom(String zoom) {
                this.zoom = zoom;
            }

            public String getRolling() {
                return rolling;
            }

            public void setRolling(String rolling) {
                this.rolling = rolling;
            }
        }
    }
}
