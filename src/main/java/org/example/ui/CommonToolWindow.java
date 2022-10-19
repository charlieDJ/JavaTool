package org.example.ui;

import org.example.listener.ConvertToFieldListener;
import org.example.listener.FormatJsonListener;

import javax.swing.*;

public class CommonToolWindow {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel convertPanel;
    private JTextArea fieldArea;
    private JPanel Jpanel;
    private JButton convert;
    private JTextArea codeArea;
    private JPanel formatJson;
    private JTextArea jsonArea;
    private JTextArea formattedJsonArea;
    private JButton convertJson;
    private JPanel examplePanel;
    private JTextField exampleText;
    private JLabel example;
    private JLabel hint;
    private JFormattedTextField hintText;

    public CommonToolWindow() {
        convert.addActionListener(new ConvertToFieldListener(this));
        convertJson.addActionListener(new FormatJsonListener(this));
    }

    public JPanel getFormatJson() {
        return formatJson;
    }

    public void setFormatJson(JPanel formatJson) {
        this.formatJson = formatJson;
    }

    public JTextArea getJsonArea() {
        return jsonArea;
    }

    public void setJsonArea(JTextArea jsonArea) {
        this.jsonArea = jsonArea;
    }

    public JTextArea getFormattedJsonArea() {
        return formattedJsonArea;
    }

    public void setFormattedJsonArea(JTextArea formattedJsonArea) {
        this.formattedJsonArea = formattedJsonArea;
    }

    public JButton getConvertJson() {
        return convertJson;
    }

    public void setConvertJson(JButton convertJson) {
        this.convertJson = convertJson;
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
