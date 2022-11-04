package org.example.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.example.util.ClipBoardUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CustomDialog extends DialogWrapper {

    JTextArea textArea;

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public CustomDialog(@Nullable Project project, String text) {
        super(project);
        setOKButtonText("复制并确定");
        setCancelButtonText("取消");
        setTitle("生成内容");
        textArea = new JTextArea();
        textArea.setText(text);
        textArea.setPreferredSize(new Dimension(400, 400));
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(textArea, BorderLayout.CENTER);
        return dialogPanel;
    }

    @Override
    protected void doOKAction() {
        ClipBoardUtils.setContent(textArea.getText());
        super.doOKAction();
    }
}
