package common;

public class Result {
    private static final long serialVersionUTD = 1L;

    private boolean success;
    private String key;
    private String value;

    public Result(boolean success, String key, String value) {
        this.success = success;
        this.value = value;
        this.key = key;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value=value;
    }

    @Override
    public String toString() {
        return "Result{" + "success? " + success + ", key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }
}
