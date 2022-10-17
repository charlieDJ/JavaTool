package org.example.listener;

import org.example.ui.CommonToolWindow;
import org.example.util.ConvertUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConvertToFieldListener extends AbstractAction {

    private final CommonToolWindow window;

    public ConvertToFieldListener(CommonToolWindow window) {
        this.window = window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String filedAreaText = window.getFieldArea().getText();
        String codeText = ConvertUtils.fieldToCode(filedAreaText);
        window.getCodeArea().setText(codeText);
    }
}
