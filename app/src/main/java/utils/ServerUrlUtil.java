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
    public static final String GET_HOT_POETRY = MY_SERVER + "/poetry/hot";

    private static final String SEARCH_POERTY = MY_SERVER + "/poetry/search";
    private static final String SEARCH_POERTY_BY_AUTHOR = MY_SERVER + "/poetry/searchByAuthor";
    private static final String SEARCH_POERTY_BY_TITLE = MY_SERVER + "/poetry/searchByTitle";
    private static final String SEARCH_POERTY_BY_TYPE = MY_SERVER + "/poetry/searchByType";

    private static final String COLLECTION_ADD = MY_SERVER + "/collection/add";
    private static final String COLLECTION_DEL = MY_SERVER + "/collection/del";
    private static final String COLLECTION_LIST = MY_SERVER + "/collection/list";


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

    public static boolean checkLoginStatus() {
        return StringUtils.isNotBlank(token) && StringUtils.isNotBlank(uid);
    }

    public static void setUser(UserSession argument) {
        userSession = argument;
        token = userSession.getToken();
        uid = userSession.getUserAccount().getUid().toString();
    }


//    public static final int USER_LIST_RANGE_ALL = 0;
//    public static final int USER_LIST_RANGE_RECOMMEND = 1;



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

    public static void getHotPoetryList(Integer page, Integer pageSize,
                                           OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("pageNo", page);
        request.put("pageSize", pageSize);

        HttpManager.getInstance().get(request, GET_HOT_POETRY, -page, listener);
    }

    public static void getSearchPoetryList(String searchText, Integer page, Integer pageSize,
                                           OnHttpResponseListener listener) {
        doSearchPoetryList(searchText, SEARCH_POERTY, page, pageSize, listener);
    }

    public static void searchPoetryListByAuthor(String author, Integer page, Integer pageSize,
                                           OnHttpResponseListener listener) {
        doSearchPoetryList(author, SEARCH_POERTY_BY_AUTHOR, page, pageSize, listener);
    }

    public static void searchPoetryListByTitle(String title, Integer page, Integer pageSize,
                                                OnHttpResponseListener listener) {
        doSearchPoetryList(title, SEARCH_POERTY_BY_TITLE, page, pageSize, listener);
    }

    public static void searchPoetryListByType(String title, Integer page, Integer pageSize,
                                               OnHttpResponseListener listener) {
        doSearchPoetryList(title, SEARCH_POERTY_BY_TYPE, page, pageSize, listener);
    }

    private static void doSearchPoetryList(String searchText, String searchType,
                                           Integer page, Integer pageSize,
                                           OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("pageNo", page);
        request.put("pageSize", pageSize);
        request.put("searchText", searchText);

        HttpManager.getInstance().get(request, searchType, -page, listener);
    }

    public static void doCollect(String poetryId, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("poetryId", poetryId);

        HttpManager.getInstance().post(request, COLLECTION_ADD, 0, listener);
    }

    public static void delCollect(String poetryId, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("poetryId", poetryId);

        HttpManager.getInstance().post(request, COLLECTION_DEL, 0, listener);
    }

    public static void getMyCollection(String search, Integer page, Integer pageSize,
                                        OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("pageNo", page);
        request.put("pageSize", pageSize);
//        request.put("orderType", 1);
        request.put("searchText", search);

        HttpManager.getInstance().get(request, COLLECTION_LIST, -page, listener);
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
