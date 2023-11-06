package com.mini.rpc.enums;

/**
 * 暴露id的方式
 *
 * @author zy
 * @date 2023/11/02
 */
public enum ExportIPTypeEnums {
    Default("default"),
    Custom("custom");

    private String type = "default";

    ExportIPTypeEnums(String type) {
        this.type = type;
    }
}
