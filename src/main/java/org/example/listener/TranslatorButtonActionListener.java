package org.example.listener;

import org.example.extension.TranslatorSetting;
import org.example.ui.TranslatorWindow;
import org.example.util.TranslatorUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class TranslatorButtonActionListener extends AbstractAction {

    private final TranslatorWindow window;
    private final Map<String, String> langMap;

    public TranslatorButtonActionListener(TranslatorWindow window) {
        this.window = window;
        langMap = new HashMap<>();
        langMap.put("中文", "zh");
        langMap.put("英文", "en");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String appid = TranslatorSetting.getInstance().appID;
        String securityKey = TranslatorSetting.getInstance().securityKey;
        if (appid == null || securityKey == null) {
            window.getTranslateTextArea().setText("请先设置appID，securityKey。\nSettings->Tools->Translator");
            return;
        }
        // 获取原语言文本、原语言、和目标翻译语言
        String originalText = window.getOriginalTextArea().getText();
        String comboBox = (String) window.getComboBox1().getSelectedItem();
        TranslatorUtils.TransResp transResult = null;
        if ("中翻英".equals(comboBox)) {
            transResult = TranslatorUtils.getTransResult(originalText, "zh", "en");
        } else {
            transResult = TranslatorUtils.getTransResult(originalText, "en", "zh");
        }
        // 翻译后，将文本设置到翻译结果文本输入框
        if (transResult.isSuccess()) {
            window.getTranslateTextArea().setText(transResult.getTransResult().get(0).getDst());
        }
    }
}
