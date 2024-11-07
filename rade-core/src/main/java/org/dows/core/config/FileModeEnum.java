package org.dows.core.config;

/**
 * 文件模式
 */
public enum FileModeEnum {
    LOCAL("local", "local", "本地"),
    CLOUD("cloud", "oss", "云存储"),
    OTHER("other", "other", "其他");

    private String value;
    private String type;
    private String descr;

    FileModeEnum(String value, String type, String descr) {
        this.value = value;
        this.type = type;
        this.descr = descr;
    }

    public String value() {
        return this.value;
    }

    public String type() {
        return this.type;
    }
}
