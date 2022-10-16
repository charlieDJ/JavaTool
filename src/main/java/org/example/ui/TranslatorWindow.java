package org.example.ui;

import org.example.listener.TranslatorButtonActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TranslatorWindow {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JPanel notePanel;
    private JPanel translatorPanel;
    private JTable noteTable;
    private JComboBox comboBox2;
    private JButton translateButton;
    private JComboBox comboBox1;
    private JTextArea originalTextArea;
    private JTextArea translateTextArea;


    public TranslatorWindow() {

        // 下拉框添加下拉选项
        comboBox1.addItem("英文");
        comboBox1.addItem("中文");
        comboBox2.addItem("中文");
        comboBox2.addItem("英文");

        // 备忘录表格属性设置
        String[] header = {"原文", "译文"};
        DefaultTableModel tableModel = new DefaultTableModel(null, header);
        noteTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        noteTable.setModel(tableModel);
        translateButton.addActionListener(new TranslatorButtonActionListener(this));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public void setTabbedPane1(JTabbedPane tabbedPane1) {
        this.tabbedPane1 = tabbedPane1;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getNotePanel() {
        return notePanel;
    }

    public void setNotePanel(JPanel notePanel) {
        this.notePanel = notePanel;
    }

    public JPanel getTranslatorPanel() {
        return translatorPanel;
    }

    public void setTranslatorPanel(JPanel translatorPanel) {
        this.translatorPanel = translatorPanel;
    }

    public JTable getNoteTable() {
        return noteTable;
    }

    public void setNoteTable(JTable noteTable) {
        this.noteTable = noteTable;
    }

    public JComboBox getComboBox2() {
        return comboBox2;
    }

    public void setComboBox2(JComboBox comboBox2) {
        this.comboBox2 = comboBox2;
    }

    public JButton getTranslateButton() {
        return translateButton;
    }

    public void setTranslateButton(JButton translateButton) {
        this.translateButton = translateButton;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public void setComboBox1(JComboBox comboBox1) {
        this.comboBox1 = comboBox1;
    }

    public JTextArea getOriginalTextArea() {
        return originalTextArea;
    }

    public void setOriginalTextArea(JTextArea originalTextArea) {
        this.originalTextArea = originalTextArea;
    }

    public JTextArea getTranslateTextArea() {
        return translateTextArea;
    }

    public void setTranslateTextArea(JTextArea translateTextArea) {
        this.translateTextArea = translateTextArea;
    }
}
