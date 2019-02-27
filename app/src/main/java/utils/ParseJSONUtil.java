package utils;


import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.ReturnMsg;
import model.UserCollection;
import model.Poetry;
import model.RecordHold;
import model.UserAccount;

public class ParseJSONUtil {

    private static  String HAS_DATA = "Succes";


    public static Poetry jsonStrToPoetry(String str){
        ReturnMsg returnMsg = JSON.parseObject(str, ReturnMsg.class);
        Poetry p = returnMsg.getResData();
//        try {
//            JSONObject obj = new JSONObject(str);
//            String reason = obj.getString("reason");
//            if (HAS_DATA.equals(reason)) {
//                obj = obj.getJSONObject("result");
//                p.setUid(obj.getString("uid"));
//                p.setTitle(obj.getString("biaoti"));
//                p.setAuthor(obj.getString("zuozhe"));
//                String zhushi = obj.getString("jieshao").replace("/r/n","");
//                if (zhushi.indexOf("：") <= 0) {
//                    zhushi += "\n\n此诗暂无注释";
//                }
//                p.setNotes(zhushi);
//                String content = obj.getString("neirong");
//                p.setContent(content.substring(content.indexOf("】")+1,content.length()).split("/n"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return p;
    }

    public static UserAccount jsonStrToUser(String loginRes) {
//        UserAccount u = new UserAccount();
//        try {
//            JSONObject obj = new JSONObject(loginRes);
//            u.setPhoneNum(obj.getString("phoneNumber"));
//            u.setNickName(obj.getString("nickName"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return u;
        return null;
    }

    public static List<RecordHold> jsonStrToRecordList(String str) {
        List<RecordHold> recordList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(str);
                JSONArray arr = obj.getJSONArray("data");
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    RecordHold r = new RecordHold();
                    r.setId(o.getInt("uid"));
                    r.setName(o.getString("nickName"));
                    r.setUploadTime(o.getString("uploadTime"));
                    r.setRecordPath(o.getString("recordPath"));
                    r.setPlayCount(o.getInt("playCount"));
                    r.setLike(o.getInt("praiseCount"));
                    recordList.add(r);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recordList;
    }

    public static List<UserCollection> jsonStrToCollectionList(String str) {
//        List<UserCollection> list = new ArrayList<>();
//        try {
//            JSONObject obj = new JSONObject(str);
//            JSONArray arr = obj.getJSONArray("data");
//            for(int i = 0; i < arr.length(); i++) {
//                JSONObject o = arr.getJSONObject(i);
//                UserCollection cm = new UserCollection();
//                cm.setId(o.getInt("uid"));
//                cm.setCollectTime(o.getString("collectTime"));
//                cm.setPoetryTitle(o.getString("poetryTitle"));
//                cm.setPoetryId(o.getString("poetryId"));
//                list.add(cm);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return list;
        return null;
    }

    public static List<RecordHold> jsonStrToMyRecordList(String str) {
        List<RecordHold> recordList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(str);
            JSONArray arr = obj.getJSONArray("data");
            for(int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                RecordHold r = new RecordHold();
                r.setId(o.getInt("uid"));
                r.setPoetryTitle(o.getString("poetryTitle"));
                r.setUploadTime(o.getString("uploadTime"));
                r.setRecordPath(o.getString("recordPath"));
                r.setPlayCount(o.getInt("playCount"));
                r.setLike(o.getInt("praiseCount"));
                recordList.add(r);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recordList;
    }
}
