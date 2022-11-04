package org.example.ui;

import org.example.listener.BizAttrConvertListener;
import org.example.util.ClipBoardUtils;

import javax.swing.*;

public class DemoWindow {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel bizAttrPanel;
    private JTextArea bizAttrArea;
    private JTextArea dataTableArea;
    private JButton parseBizAttr;
    private JButton copyTable;



    public DemoWindow() {
        parseBizAttr.addActionListener(new BizAttrConvertListener(this));
        copyTable.addActionListener(actionEvent -> ClipBoardUtils.setContent(dataTableArea.getText()));
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

    public JPanel getBizAttrPanel() {
        return bizAttrPanel;
    }

    public void setBizAttrPanel(JPanel bizAttrPanel) {
        this.bizAttrPanel = bizAttrPanel;
    }

    public JTextArea getBizAttrArea() {
        return bizAttrArea;
    }

    public void setBizAttrArea(JTextArea bizAttrArea) {
        this.bizAttrArea = bizAttrArea;
    }

    public JTextArea getDataTableArea() {
        return dataTableArea;
    }

    public void setDataTableArea(JTextArea dataTableArea) {
        this.dataTableArea = dataTableArea;
    }

    public JButton getParseBizAttr() {
        return parseBizAttr;
    }

    public void setParseBizAttr(JButton parseBizAttr) {
        this.parseBizAttr = parseBizAttr;
    }

    public JButton getCopyTable() {
        return copyTable;
    }

    public void setCopyTable(JButton copyTable) {
        this.copyTable = copyTable;
    }
}
