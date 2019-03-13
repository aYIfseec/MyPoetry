package common;

public enum CommentListOrder {

    ORDER_BY_HEAT(1, "heatCount desc"),
    ORDER_BY_TIME_DESC(2, "create_time desc"),
    Null(-1, null);


    private Integer code;
    private String value;

    CommentListOrder(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static CommentListOrder valueOfCode(Integer code) {
        for (CommentListOrder obj : CommentListOrder.values()) {
            if (java.util.Objects.equals(obj.code, code)) {
                return obj;
            }
        }
        return Null;
    }

    public static CommentListOrder valueOfValue(String value) {
        for (CommentListOrder obj : CommentListOrder.values()) {
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
