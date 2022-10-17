package org.example.ui;

import org.example.listener.ConvertToFieldListener;

import javax.swing.*;

public class CommonToolWindow {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel convertPanel;
    private JTextArea fieldArea;
    private JPanel Jpanel;
    private JButton convert;
    private JTextArea codeArea;

    public CommonToolWindow() {
        convert.addActionListener(new ConvertToFieldListener(this));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public void setTabbedPane1(JTabbedPane tabbedPane1) {
        this.tabbedPane1 = tabbedPane1;
    }

    public JPanel getConvertPanel() {
        return convertPanel;
    }

    public void setConvertPanel(JPanel convertPanel) {
        this.convertPanel = convertPanel;
    }

    public JTextArea getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(JTextArea fieldArea) {
        this.fieldArea = fieldArea;
    }

    public JPanel getJpanel() {
        return Jpanel;
    }

    public void setJpanel(JPanel jpanel) {
        Jpanel = jpanel;
    }

    public JButton getConvert() {
        return convert;
    }

    public void setConvert(JButton convert) {
        this.convert = convert;
    }

    public JTextArea getCodeArea() {
        return codeArea;
    }

    public void setCodeArea(JTextArea codeArea) {
        this.codeArea = codeArea;
    }
}
