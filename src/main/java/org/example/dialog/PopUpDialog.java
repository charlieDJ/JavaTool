package org.example.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PopUpDialog extends DialogWrapper {
    /**
     * 弹出框文本内容
     */
    private String content;

    public static PopUpDialog getInstance(Project project){
        return new PopUpDialog(project);
    }

    public static PopUpDialog getInstance(Project project,String content){
        return new PopUpDialog(project, content);
    }

    public PopUpDialog(@Nullable Project project) {
        super(project);
        init();
    }

    public PopUpDialog(@Nullable Project project, String content) {
        super(project);
        this.content = content;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return new JLabel(content);
    }
}
