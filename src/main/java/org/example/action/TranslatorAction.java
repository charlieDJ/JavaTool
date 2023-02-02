package org.example.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang3.StringUtils;
import org.example.extension.TranslatorCache;
import org.example.extension.TranslatorSetting;
import org.example.extension.TranslatorToolsWindow2;
import org.example.util.TranslatorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TranslatorAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranslatorSetting setting = TranslatorSetting.getInstance();
        String appId = setting.getAppID();
        String securityKey = setting.getSecurityKey();
        if (StringUtils.isEmpty(appId) ||  StringUtils.isEmpty(securityKey)) {
            Notifications.Bus.notify(new Notification("Translator", "小天才翻译机", "请先设置appID，securityKey。\nSettings->Tools->Translator", NotificationType.ERROR), e.getProject());
            return;
        }
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        String text = editor.getSelectionModel().getSelectedText();
        // 获取到持久化数据对象
        Map<String, String> transCache = TranslatorCache.getInstance(e.getProject()).transCache;
        String transResult;
        // 缓存存在查询缓存，不存在通过 API 查询
        if (transCache.containsKey(text)) {
            transResult = transCache.get(text);
        } else {
            TranslatorUtils.TransResp transResp = TranslatorUtils.getTransResult(text, "auto", "zh");
            if (transResp.isSuccess()) {
                String dst = transResp.getTransResult().get(0).getDst();
                transCache.put(text, dst);
                TranslatorToolsWindow2.addNote(text, dst);
                transResult = dst;
            } else {
                transResult = transResp.getMessage();
            }
        }
        Notifications.Bus.notify(new Notification("Translator", "小天才翻译机", transResult, NotificationType.INFORMATION), e.getProject());
    }
}
