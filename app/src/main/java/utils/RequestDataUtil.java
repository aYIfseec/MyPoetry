package utils;


import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import common.ResourceType;
import common.UserCollectionListOrder;
import model.Poetry;
import model.UserSession;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;


public class RequestDataUtil {

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

    public static final int COLLECTION_ADD_REQUEST_CODE = -1;
    public static final int COLLECTION_DEL_REQUEST_CODE = -2;
//    public static final Integer COLLECTION_DEL_REQUEST_CODE = -3;

    public static final int DEFAULT_REQUEST_CODE = 0;
    public static final int COMMENT_UPLOAD = 100;
    public static final int UPLOAD_AUDIO_CODE = 101;
    public static final int DO_COMMENT_CODE = 102;
    public static final int DO_READ_CODE = 103;
    public static final int DO_LIKE_CODE = 104;
    public static final int DO_CANCEL_LIKE_CODE = 105;

    private static final String COLLECTION_ADD = MY_SERVER + "/collection/add";
    private static final String COLLECTION_DEL = MY_SERVER + "/collection/del";
    private static final String COLLECTION_LIST = MY_SERVER + "/collection/list";


    private static final String COMMENT_ADD = MY_SERVER + "/comment/add";
    private static final String COMMENT_DEL = MY_SERVER + "/comment/del";
    private static final String COMMENT_LIST = MY_SERVER + "/comment/poetry/list";
    private static final String MY_COMMENT_LIST = MY_SERVER + "/comment/user/list";

    private static final String COMMENT_DO_LIKE = MY_SERVER + "/comment/like";
    private static final String COMMENT_DO_READ = MY_SERVER + "/comment/read";
    private static final String COMMENT_CANCEL_LIKE = MY_SERVER + "/comment/cancel/like";


    private static final String UPLOAD_AUDIO = MY_SERVER + "/resource/audio/upload";


    public static final int smallPageSize = 10;
    public static final int middlePageSize = 25;
    public static final int largePageSize = 40;


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

    public static String getMyUploadRecord(String phoneNumber, int page) {
        return MY_SERVER + "getMyUploadRecord?phoneNumber=" + phoneNumber + "&page=" + page;
    }

    public static String getPlayNetPath(String recordPath) {
        return "https://guwen-1252396323.cos.ap-chengdu.myqcloud.com/guwen/20180913143329337.mp3";
    }

    public static String getDoDeleteUrl(int recordId) {
        return MY_SERVER + "updateRecord?do=doDelete&recordId=" + recordId;
    }


    public static void getPoetry(String poetryId, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);

        if (StringUtils.isBlank(poetryId)) {
            HttpManager.getInstance().get(request, GET_TODAY_POETRY, DEFAULT_REQUEST_CODE, listener);
        } else {
            request.put("poetryId", poetryId);
            HttpManager.getInstance().get(request, GET_POETRY_BY_ID, DEFAULT_REQUEST_CODE, listener);
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

    public static void getCommentForPoetry(Long poetryId, Integer page, Integer pageSize,
                                           OnHttpResponseListener listener) {

        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("pageNo", page);
        request.put("pageSize", pageSize);
        request.put("poetryId", poetryId.toString());

        HttpManager.getInstance().get(request, COMMENT_LIST, -page, listener);
    }

    public static void doPlay(String commentId, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("commentId", commentId);

        HttpManager.getInstance().post(request, COMMENT_DO_READ, DO_READ_CODE, listener);
    }

    public static void doLike(String commentId, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("commentId", commentId);

        HttpManager.getInstance().post(request, COMMENT_DO_LIKE, DO_LIKE_CODE, listener);
    }

    public static void cancelLike(String commentId, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("commentId", commentId);

        HttpManager.getInstance().post(request, COMMENT_CANCEL_LIKE, DO_CANCEL_LIKE_CODE, listener);
    }

    public static void doCollect(String poetryId, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("poetryId", poetryId);

        HttpManager.getInstance().post(request, COLLECTION_ADD, COLLECTION_ADD_REQUEST_CODE, listener);
    }

    public static void delCollect(Integer collectId, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("collectId", collectId);

        HttpManager.getInstance().post(request, COLLECTION_DEL, COLLECTION_DEL_REQUEST_CODE, listener);
    }

    public static void getMyCollection(String search, UserCollectionListOrder orderBy, Integer page, Integer pageSize,
                                       OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("pageNo", page);
        request.put("pageSize", pageSize);
        request.put("orderType", orderBy.getCode());
        request.put("searchText", search);

        HttpManager.getInstance().get(request, COLLECTION_LIST, -page, listener);
    }



    public static void doUpload(String filePath, int uploadCode, OnHttpResponseListener listener) {
        File file = new File(filePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"))
                .addFormDataPart("uid", uid)
                .addFormDataPart("token", token)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder().url(RequestDataUtil.UPLOAD_AUDIO)
                .header("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryfgq4K0kTMdbpEgHQ")
                .post(multipartBody)//传参数、文件或者混合，改一下就行请求体就行
                .build();


        String url = null;
        int requestCode = 0;

        if (UPLOAD_AUDIO_CODE == uploadCode) {
            url = UPLOAD_AUDIO;
            requestCode = UPLOAD_AUDIO_CODE;
        }

        HttpManager.getInstance().post(request, url, requestCode, listener);
    }

    public static void uploadRecord(String filePath, final OnHttpResponseListener listener) {
        doUpload(filePath, UPLOAD_AUDIO_CODE, listener);
    }

    public static void doComment(Poetry poetry, String comment, String filePath, ResourceType resourceType, OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("uid", uid);
        request.put("token", token);
        request.put("parentId", poetry.getPoetryId());
        request.put("poetryTitle", poetry.getTitle());
        request.put("content", comment);
        request.put("resourceUrl", filePath);
        request.put("resourceType", resourceType.getCode());

        StringBuilder url = new StringBuilder(COMMENT_ADD);
        url.append("?").append("uid=").append(uid).append("&").append("token=").append(token);

        HttpManager.getInstance().post(request, url.toString(), true, DO_COMMENT_CODE, listener);
    }

    public static void doRegister(String name, String phoneNum, String password,
                                  OnHttpResponseListener listener) {

        Map<String, Object> request = new HashMap<>();
        request.put("nickName", name);
        request.put("phone", phoneNum);
        request.put("pwd", EncryptUtil.encrypt(password));

        HttpManager.getInstance().post(request, REGISTER_REQUEST, DEFAULT_REQUEST_CODE, listener);
    }


    public static void doLogin(String phone, String password, OnHttpResponseListener listener) {

        Map<String, Object> request = new HashMap<>();
        request.put("phone", phone);
        request.put("pwd", EncryptUtil.encrypt(password));

        HttpManager.getInstance().post(request, LOGIN_REQUEST, DEFAULT_REQUEST_CODE, listener);
    }

}