package utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import model.UserSession;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;


public class ServerUrlUtil {

    private static final String APPKEY = "dfcb2dfa0f2b46f39518edda85ef8b4a";
    public static final String RANDOM_GET_POERTY_URL = "http://api.avatardata.cn/TangShiSongCi/Random?key=" + APPKEY;
//    private static final String SEARCH_POERTY_URL = "http://api.avatardata.cn/TangShiSongCi/Search?key=" + APPKEY + "&";
    private static final String GET_POERTY_BY_ID_URL = "http://api.avatardata.cn/TangShiSongCi/LookUp?key=" + APPKEY + "&";

    /**
     * 服务器地址
     */
    private static final String DEV_SERVER = "http://10.0.1.187:8080";
    private static final String PROD_SERVER = "http://203.195.176.170:8080";
    private static final String MY_SERVER = PROD_SERVER;

    /**
     * 后端接口
     */
    private static final String LOGIN_REQUEST = MY_SERVER + "/user/login";
    private static final String REGISTER_REQUEST = MY_SERVER + "/user/register";
    public static final String UPLOAD_FILE = MY_SERVER + "/audio/upload";
    public static final String GET_TODAY_POETRY = MY_SERVER + "/poetry/today";
    public static final String GET_POETRY_BY_ID = MY_SERVER + "/poetry/getById";
    private static final String SEARCH_POERTY = MY_SERVER + "/poetry/search";
    private static final String SEARCH_POERTY_BY_AUTHOR = MY_SERVER + "/poetry/searchByAuthor";


    /**
     * 用户token
     */
    private static UserSession userSession = null;
    private static String token = "052ec949-c13f-4fe4-a94e-cdfc04ef247d";
    private static String uid = "14459394188115968";

    public static String getUserName() {
        if (userSession == null || userSession.getUserAccount() == null) {
            return "";
        }
        return userSession.getUserAccount().getNickName();
    }

    public static void setUser(UserSession argument) {
        userSession = argument;
        token = userSession.getToken();
        uid = userSession.getUserAccount().getUid().toString();
    }


    public static final int USER_LIST_RANGE_ALL = 0;
    public static final int USER_LIST_RANGE_RECOMMEND = 1;



    public static String getCollectionListUrl(String phoneNumber, int i) {
        return MY_SERVER + "getCollectionList?phoneNumber=" + phoneNumber + "&page=" + i;
    }

    public static String getCollectUrl(String phoneNumber, String poetryId, String poetryTitle) {
        try {
            poetryTitle = URLEncoder.encode(poetryTitle,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return MY_SERVER + "collect?do=collect&phoneNumber=" + phoneNumber + "&poetryId=" + poetryId + "&poetryTitle="+poetryTitle;
    }

    public static String getCancelCollectUrl(String collectId) {
        int id = Integer.parseInt(collectId);
        return MY_SERVER + "collect?do=cancelCollect&collectId=" + id;
    }

    public static String getDoPariseUrl(String recordId) {
        int id = Integer.parseInt(recordId);
        return MY_SERVER + "updateRecord?do=doPraise&recordId=" + id;
    }

    public static String getDoPlayUrl(String recordId) {
        int id = Integer.parseInt(recordId);
        return MY_SERVER + "updateRecord?do=doPlay&recordId=" + id;
    }

    public static String getMyUploadRecord(String phoneNumber, int page) {
        return MY_SERVER + "getMyUploadRecord?phoneNumber=" + phoneNumber + "&page=" + page;
    }

    public static String getPlayNetPath(String recordPath) {
        return MY_SERVER + "upload/" + recordPath;
    }

    public static String getDoDeleteUrl(int recordId) {
        return MY_SERVER + "updateRecord?do=doDelete&recordId=" + recordId;
    }


    public static void getPoetry(String poetryId, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);

        if (StringUtils.isBlank(poetryId)) {
            HttpManager.getInstance().get(request, GET_TODAY_POETRY, 0, listener);
        } else {
            request.put("poetryId", poetryId);
            HttpManager.getInstance().get(request, GET_POETRY_BY_ID, 0, listener);
        }
    }

    public static void getSearchPoetryList(String searchText, Integer page, Integer pageSize,
                                           OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("pageNo", page);
        request.put("pageSize", pageSize);
        request.put("searchText", searchText);

        HttpManager.getInstance().get(request, SEARCH_POERTY_BY_AUTHOR, 0, listener);
    }


    public static void doRegister(String name, String phoneNum, String password,
                                  OnHttpResponseListener listener) {

        Map<String, Object> request = new HashMap<>();
        request.put("nickName", name);
        request.put("phone", phoneNum);
        request.put("pwd", EncryptUtil.encrypt(password));

        HttpManager.getInstance().post(request, REGISTER_REQUEST, 0, listener);
    }


    public static void doLogin(String phone, String password, OnHttpResponseListener listener) {

        Map<String, Object> request = new HashMap<>();
        request.put("phone", phone);
        request.put("pwd", EncryptUtil.encrypt(password));

        HttpManager.getInstance().post(request, LOGIN_REQUEST, 0, listener);
    }

}
