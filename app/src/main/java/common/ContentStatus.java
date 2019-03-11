package common;

public enum ContentStatus {
    FROZEN(-3, "冻结"),
    DEL(-2, "已删除"),

    NOT_PASS(-1, "审核不通过"),
    UPLOAD(0, "上传中"),
    UPLOAD_FAIL(1, "上传失败"),
    AUDIT(2, "审核中"),

    NORMAL(3, "正常"),

    Null(-100, null);


    private Integer code;
    private String value;

    ContentStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ContentStatus valueOfCode(Integer code) {
        for (ContentStatus obj : ContentStatus.values()) {
            if (java.util.Objects.equals(obj.code, code)) {
                return obj;
            }
        }
        return Null;
    }

    public static ContentStatus valueOfValue(String value) {
        for (ContentStatus obj : ContentStatus.values()) {
            if (java.util.Objects.equals(obj.value, value)) {
                return obj;
            }
        }
        return Null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
