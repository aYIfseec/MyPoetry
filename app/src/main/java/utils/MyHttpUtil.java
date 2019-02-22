package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyHttpUtil {

    private static final String APPKEY = "dfcb2dfa0f2b46f39518edda85ef8b4a";
    public static final String RANDOM_GET_POERTY_URL = "http://api.avatardata.cn/TangShiSongCi/Random?key=" + APPKEY;
    private static final String SEARCH_POERTY_URL = "http://api.avatardata.cn/TangShiSongCi/Search?key=" + APPKEY + "&";
    private static final String GET_POERTY_BY_ID_URL = "http://api.avatardata.cn/TangShiSongCi/LookUp?key=" + APPKEY + "&";

    private static final String MY_SERVER = "http://118.89.164.202/MyPoetry/";//TODO 真机测试时改成服务器ip
    private static final String LOGIN_REQUEST = MY_SERVER + "userLogin?";
    private static final String REGISTER_REQUEST = MY_SERVER + "userRegister?";
    public static final String UPLOAD_FILE = MY_SERVER + "recordUpload";
    private static final String GET_RECORD = MY_SERVER + "getRecordList?";


    /****** test ********/

    public static final String GET_TODAY_POETRY = "http://10.0.1.187:8080/poetry/today";

    /*******   *   *****/

    public static String getRecordListUrl(String poetryId, int page) {
        return GET_RECORD + "poetryId=" + poetryId + "&page=" + page;
    }

    /**
     *
     * @param keyword
     * @param page 默认1
     * @param rows <= 50  默认20
     * @return
     */
    public static String getValidUrl(String keyword, int page, int rows) {
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SEARCH_POERTY_URL + "keyWord=" + keyword + "&page=" + page + "&rows="+rows;
    }

    public static String getValidUrl(String poetryId) {
        return GET_POERTY_BY_ID_URL + "id=" + poetryId;
    }

    public static String getLoginUrl(String phoneNum, String password) {
        return LOGIN_REQUEST + "phoneNum=" + phoneNum + "&password=" + password;
    }

    public static String getRegisterUrl(String name, String phoneNum, String password) {
        try {
            name = URLEncoder.encode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return REGISTER_REQUEST + "phoneNum=" + phoneNum + "&password=" + password + "&name=" + name;
    }


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
}
