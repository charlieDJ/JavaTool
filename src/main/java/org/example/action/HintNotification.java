package org.example.action;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

public class HintNotification extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // getData 方法用于获取 editor 对象，使用其他 PlatformDataKeys，
        // getData 方法还能用于获取 project 、光标、当前的编辑的文件对象。
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String text = editor.getSelectionModel().getSelectedText();
        HintManager.getInstance().showInformationHint(editor, text + System.currentTimeMillis());
    }
}
