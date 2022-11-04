package org.example.listener;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.Attribute;
import org.example.entity.InputAttribute;
import org.example.ui.DemoWindow;
import org.example.util.TranslatorUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BizAttrConvertListener extends AbstractAction {

    public static final String NON_REQUIRED_CRITERIA = "非必填";
    public static final String REQUIRED = "是";
    public static final String NON_REQUIRED = "否";


    private final DemoWindow window;

    public BizAttrConvertListener(DemoWindow window) {
        this.window = window;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JTextArea textArea = window.getBizAttrArea();
        String bizText = textArea.getText();
        if(StringUtils.isEmpty(bizText)){
            return;
        }
        List<String> bizAttrs = Arrays.stream(bizText.split("\n"))
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        List<InputAttribute> attributes = new ArrayList<>();
        for (String text : bizAttrs) {
            List<String> bizAttributes = Arrays.stream(text.split("\t"))
                    .collect(Collectors.toList());
            InputAttribute attribute = new InputAttribute();
            for (int i = 0; i < bizAttributes.size(); i++) {
                if (i == 0) {
                    attribute.setName(bizAttributes.get(i));
                }
                if (i == 1) {
                    attribute.setDesc(bizAttributes.get(i));
                }
            }
            attributes.add(attribute);
        }
        List<Attribute> attributeList = getAttributes(attributes);
        List<String> outputLines = new ArrayList<>();
        AtomicInteger integer = new AtomicInteger(1);
        attributeList.forEach(e -> {
            int increment = integer.getAndIncrement();
            outputLines.add(e.format(String.valueOf(increment)));
        });
        String join = String.join("\n", outputLines);
        window.getDataTableArea().setText(join);
    }

    private List<Attribute> getAttributes(List<InputAttribute> inputAttributes) {

        return inputAttributes.stream()
                .map(e -> {
                    Attribute attribute = new Attribute();
                    String name = e.getName();
                    attribute.setName(name);
                    attribute.setType("String");
//                    String codeValue = ops.get(name);
                    String codeValue = "";
                    TranslatorUtils.TransResp transResult = TranslatorUtils.getTransResult(name, "zh", "en");
                    if(transResult.isSuccess()){
                        String code = transResult.getTransResult().get(0).getDst();
                        codeValue = formatCode(code);
                    }
                    attribute.setCode(codeValue);
                    attribute.setLov("");
                    attribute.setLength("32");
                    String desc = e.getDesc();
                    if (StringUtils.isEmpty(desc)) {
                        attribute.setRequired(NON_REQUIRED);
                        attribute.setInputMethod("");
                    } else {
                        String replace = desc.replace("非必填，", "")
                                .replace("必填，", "");
                        attribute.setInputMethod(replace);
                        if (desc.contains("下拉")) {
                            int index = desc.indexOf("下拉") + 2;
                            String substring = desc.substring(index);
                            attribute.setLov(substring);
                        }
                        if (desc.contains(NON_REQUIRED_CRITERIA)) {
                            attribute.setRequired(NON_REQUIRED);
                        } else {
                            attribute.setRequired(REQUIRED);
                        }
                    }
                    return attribute;
                }).collect(Collectors.toList());
    }

    private String formatCode(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] s1 = str.trim().split(" ");
        for (int i = 0; i < s1.length; i++) {
            String s = s1[i];
            if (i != 0) {
                result.append(WordUtils.capitalize(s));
            } else {
                result.append(WordUtils.uncapitalize(s));
            }
        }
        return result.toString();
    }
}
