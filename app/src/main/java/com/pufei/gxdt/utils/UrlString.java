package com.pufei.gxdt.utils;

/**
 * Created by wangwenzhang on 2016/11/23.
 */
public class UrlString {
    public static final String INCOME_URL = "https://jiakao.xianwan.com/task/task/income";
    public static final String WITHDRAW_URL = "https://jiakao.xianwan.com/task/member/withdraw";
    public static final String TRADE_RECORD__URL = " https://jiakao.xianwan.com/task/member/bill";
    public static final String GET_CODE_URL="https://jiakao.xianwan.com/task/login/get_vcode";
    private static final String TAG="http://erp.xianwan.com";
    //private static final String TAG="http://test.xianwan.com";
    public static final String DO_TASK=TAG+"/api/v4/Task/doTask";
    public static final String SPECIAL_MISSION=TAG+"/api/v4/Task/getSpecial";
    public static final String DAY_MISSION=TAG+"/api/v4/Task/getDaily";
    public static final String EXCHANGE_PRODUCT=TAG+"/api/v4/Score/exchangeProduct";
    public static final String EXCHANGE_HISTORY=TAG+"/api/v4/Score/exchangeRecord";
    public static final String MISSION_STORE=TAG+"/api/v4/Score/getProductList";
    public static final String ADVERT_SECOND="http://api.tuimao.com//get_creative?did=418547200&pid=1451&ct=1&tpt=2&n=5&s=";
    public static final String ADVERT_DETAIL="http://api.tuimao.com//get_creative?did=418547200&pid=1401&ct=1&tpt=2&n=1&s=0";
    public static final String ADVERT_JOKE="http://api.tuimao.com//get_creative?did=418547200&pid=1402&ct=1&tpt=2&n=1&s=0";
    public static final String ADVERT_FIND="http://api.tuimao.com//get_creative?did=418547200&pid=1403&ct=1&tpt=2&n=5&s=0";
    public static final String ADVERT_FLASH="http://api.tuimao.com//get_creative?did=418547200&pid=1400&ct=1&tpt=2&n=1&s=0";
    public static final String ADVERT_START="http://api.tuimao.com//get_creative?did=418547200&pid=1467&ct=1&tpt=2&n=1&s=0";
    public static final String ADVERT_HEARDER="http://api.tuimao.com/get_creative?did=418547200&pid=1369&ct=1&tpt=2&n=5&s=0";
    public static final String REPORT=TAG+"/api/v3/OptionImg/DealImg/";
    public static final String FIND_PACKGE=TAG+"/api/v4/Category/getRandList";
    public static final String FIND_FACE=TAG+"/api/v4/AmuseImages/getRandNewList";
    public static final String FIND_SHARE=TAG+"/api/v3/User/getAllImage";
    public static final String crunchies=TAG+"/api/v3/User/scoreRanking";
    public static final String delteimage=TAG+"/api/v3/User/deleteImages";
    public static final String myenshrine=TAG+"/api/v3/User/myImages";
    public static final String enshrine=TAG+"/api/v3/User/addImages";
    public static final String LOADING=TAG+"/api/v3/User/login";
    public static final String down="http://android.myapp.com/myapp/detail.htm?apkName=com.pufei.gxdt";
    public static final String Flash =TAG+"/api/v4/Category/getSliders";
    public static final String Search=TAG+"/api/v4/Category/categorySearch";
    public static final String QQUrl =TAG+"/api/v3/ConnectUs/getQQ";
    public static final String Send =TAG+"/api/v3/Advice/getAdvice";
    public static final String Template=TAG+"/api/v4/AmuseImages/makeList";
    public static final String ClassiUrl=TAG+"/api/v4/Category/getThemeList";
    public static final String HotPicture =TAG+"/api/v4/AmuseImages/getNewList";
    public static final String PictureReList=TAG+"/api/v4/AmuseImages/getList";
    public static final String recommend =TAG+"/api/v4/Category/getList";
    public static final String Jokedetail11=TAG+"/api/v3/Article/getDetail/";
    public static final String JokeList=TAG+"/api/v3/Article/getList/";
    public static final String friststart=TAG+"/api/apiStart/firstStart";
    public static final String Update=TAG+"/api/apiStart/version";
    public static final String Jokedta="http://s.budejie.com/topic/tag-topic/64/hot/bs0315-iphone-4.5/0-20.json";
    public static final String Classification=TAG+"/api/v1/Category/getList";
    public static final String PictureList=TAG+"/api/v1/AmuseImages/getList";
    public static final String BindTel=TAG+"/api/v3/User/mobileBind";
    public static final String BindSuccess=TAG+"/api/v3/User/verifyCode";
    public static final String Content="搞笑斗图大师是专门为了当代青年群体，研发的一款娱乐实用的软件，里面包含了斗图和下载图片，还有笑话等娱乐功能，各类装逼表情应有尽有，专为千万斗图达人精心准备，我们不生产图，我们只是装逼能量的搬运工";
    public static final String SIGN_IN=TAG+"/api/v3/User/manageScore";
    public static final String GETSCORE=TAG+"/api/v3/User/getScore";
    public static final String SEND="";
    public static final String Make_UP="make__updata";
    public static final String  SHARE_TAG="SIGN";
    public static final String ADVERT="{\"message\":\"OK\",\"results\":[{\"click_monitor_url\":\"\",\"creative_desc\":\"测试文字描述1\",\"creative_id\":\"3080\",\"creative_type\":\"3\",\"dest_url\":\"https://oa-markhor.data.aliyun.com/click?b=0354352231885f80544aad7fa0c4f26a22fd2ac1abf553f848612ac048a516e727bc493e6d8d523423f9344831e3c0483624a2d06b2aa6d68978e46e9954d9ffef1258fb443acab0a85eb159df610733fcdfa7442ca27adf6498a3494aef6e927f365a939b0a23862eecd20aa2fad88e32b92ef86f7de61de98e8f504bcde7b7ad7fba9fdcd5edc56dc2206220dd4f68ca653ed598f2040ea8be1ef9325a8f5c\",\"download_url\":\"\",\"hunt\":false,\"monitor_url\":\"\",\"title\":\"测试文字标题1\",\"user_define\":\"{\\\"body\\\":{\\\"imgs\\\":[{\\\"img1\\\":\\\"http://imgoacdn.aliyuncs.com/MzNjY2IzYTAtN2JlNi00YWUzLWI0YzMtODM5ODJiMTcxNTRl_418547200_MzE5KjIzOA%3D%3D.png\\\",\\\"img2\\\":\\\"http://imgoacdn.aliyuncs.com/YzMxMzkwODMtZTNhOS00ZjU3LTlhMmEtNDA5NGNiYTEyZGNi_418547200_MzE5KjIzOA%3D%3D.png\\\",\\\"img3\\\":\\\"http://imgoacdn.aliyuncs.com/MGM2OTIxYzEtZDM0OC00ZTExLWE5MTUtOTIwNWQwMDVjMjBj_418547200_MzE5KjIzOA%3D%3D.png\\\"}]},\\\"id\\\":160,\\\"category\\\":1,\\\"title\\\":\\\"3图信息流\\\"}\"}],\"status\":\"200\",\"t\":\"0\"}";
    public static final String START_ADVERT="{\"message\":\"OK\",\"results\":[{\"click_monitor_url\":\"\",\"creative_id\":\"3205\",\"creative_type\":\"4\",\"dest_url\":\"https://oa-markhor.data.aliyun.com/click?b=27cd43cc03483bff47136000c8f3c6d75f3e6b568ec9abb8b187139e2071a2b3550ec559d475c0ddf1626ef4472a3473b4b020e7269e7a0dead6c6d16c861b28fac0e426de555d966c4b8f80f1c4689dc3b4343d366944106de54c28f25dffc93acb9a528af9aaa5bf938c0f25603818db61e22e37c73a409fba226ce4c9d14c30229abcfa740cc3c6fd75c505fe3027f5efeb923b1e6090078ccd6a8b395164\",\"download_url\":\"\",\"hunt\":false,\"img_path\":\"http://imgoacdn.aliyuncs.com/Y2o3c2llOW0wMDAxbDNjNXF6MzZseHV4Mg%3D%3D_418547200_NzIwKjEyODA%3D.jpg\",\"monitor_url\":\"\",\"user_define\":\"{\\\"body\\\":{\\\"\\\":\\\"\\\"},\\\"name\\\":-1,\\\"title\\\":\\\"不使用模板\\\"}\"}],\"status\":\"200\",\"t\":\"0\"}";
    public static final String FIND_ADVERT="{\"message\":\"OK\",\"results\":[{\"click_monitor_url\":\"\",\"creative_id\":\"3205\",\"creative_type\":\"4\",\"dest_url\":\"https://oa-markhor.data.aliyun.com/click?b=83d638fc6778bb67a4af2abb2776b0d47c6770309425b985471e98a98c9d28a7c6df9c46767ec5edb5e74c2247d6a36e7e095d58baf3991e80d5dcfadc882968db6be39544a785a2ea0fbdd75019363869e6992de3f691bb93796fb08851506c0bfdca44058361242fa0d997eb1ec3fbc1ca28799d9892b766954b5efb83dc3ce379e28134a145ac8ac4ee478e4acea254c62d6cb42d2fe87b0bf3426364d058\",\"download_url\":\"\",\"hunt\":false,\"img_path\":,\"monitor_url\":\"\",\"user_define\":\"{\\\"body\\\":{\\\"\\\":\\\"\\\"},\\\"name\\\":-1,\\\"title\\\":\\\"不使用模板\\\"}\"}],\"status\":\"200\",\"t\":\"0\"}";
}
