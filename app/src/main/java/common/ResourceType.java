package common;

public enum ResourceType {

    AUDIO(1, "audio"),
    VIDEO(2, "video"),
    NOT(0, ""),
    Null(-1, null);


    private Integer code;
    private String value;

    ResourceType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ResourceType valueOfCode(Integer code) {
        for (ResourceType obj : ResourceType.values()) {
            if (java.util.Objects.equals(obj.code, code)) {
                return obj;
            }
        }
        return Null;
    }

    public static ResourceType valueOfValue(String value) {
        for (ResourceType obj : ResourceType.values()) {
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
