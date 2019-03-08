package common;

public enum UserCollectionListOrder {

    ORDER_BY_TITLE(1, "title"),
    ORDER_BY_TITLE_DESC(2, "title desc"),
    ORDER_BY_TIME(3, "create_time"),
    ORDER_BY_TIME_DESC(4, "create_time desc"),
    Null(-1, null);


    private Integer code;
    private String value;

    UserCollectionListOrder(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static UserCollectionListOrder valueOfCode(Integer code) {
        for (UserCollectionListOrder obj : UserCollectionListOrder.values()) {
            if (java.util.Objects.equals(obj.code, code)) {
                return obj;
            }
        }
        return Null;
    }

    public static UserCollectionListOrder valueOfValue(String value) {
        for (UserCollectionListOrder obj : UserCollectionListOrder.values()) {
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
