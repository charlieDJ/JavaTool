package org.example.entity;

/**
 * java字段元属性
 */
public class Meta {
    /**
     * 字段编码
     */
    private String code;
    /**
     * 字段名称、注释
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
