package org.example.extension;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.example.ui.TranslatorWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;

public class TranslatorToolsWindow2 implements ToolWindowFactory {

    private static JTable table;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // ContentFactory 在 IntelliJ 平台 SDK 中负责UI界面的管理
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 创建我们的工具栏界面
        // TranslatorNote note = new TranslatorNote();
        // table = note.getTable();
        TranslatorWindow translatorWindow = new TranslatorWindow();
        table = translatorWindow.getNoteTable();
        if (Objects.nonNull(table)) {
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            if (tableModel.getRowCount() == 0) {
                TranslatorCache.getInstance(project).transCache.forEach(TranslatorToolsWindow2::addNote);
            }
        }
        // 在界面工厂中创建翻译插件的界面
        Content content = contentFactory.createContent(translatorWindow.getMainPanel(), "", false);
        // 将被界面工厂代理后创建的 content，添加到工具栏窗口管理器中
        toolWindow.getContentManager().addContent(content);
    }

    // 基于 Swing 实现的窗口类
    static class TranslatorNote {
        // Swing 中的滑动窗口视图
        private final JScrollPane notePanel;
        // Swing 中的表格视图，该表格视图用于展示翻译结果
        private final JTable table;

        public TranslatorNote() {
            // 设置表格的表头
            String[] header = {"原文", "译文"};
            DefaultTableModel tableModel = new DefaultTableModel(null, header);
            this.table = new JTable();
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            this.table.setModel(tableModel);
            this.notePanel = new JBScrollPane(table);
            this.notePanel.setSize(200, 800);
        }

        public JScrollPane getNotePanel() {
            return notePanel;
        }

        public JTable getTable() {
            return table;
        }
    }

    // addNote 方法将翻译插件的原文与译文加入到工具栏窗口的表格视图中
    public static void addNote(String from, String to) {
        if (table == null) {
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(new Object[]{from, to});
    }
}
