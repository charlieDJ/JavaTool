package org.example.entity;


import org.apache.commons.lang3.StringUtils;

public class Attribute {
    private String name;
    private String code;
    private String type;
    private String length;
    private String required;
    private String lov;
    private String inputMethod;


    public String format(String number) {
        if (StringUtils.isEmpty(number)) {
            return String.join("\t", name, code, type, length, required, lov, inputMethod);
        }
        return String.join("\t", number, name, code, type, length, required, lov, inputMethod);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getLov() {
        return lov;
    }

    public void setLov(String lov) {
        this.lov = lov;
    }

    public String getInputMethod() {
        return inputMethod;
    }

    public void setInputMethod(String inputMethod) {
        this.inputMethod = inputMethod;
    }
}
