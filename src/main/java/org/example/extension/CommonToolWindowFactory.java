package org.example.extension;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.example.ui.CommonToolWindow;
import org.jetbrains.annotations.NotNull;

public class CommonToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // ContentFactory 在 IntelliJ 平台 SDK 中负责UI界面的管理
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        CommonToolWindow commonToolWindow = new CommonToolWindow();
        // 在界面工厂中创建翻译插件的界面
        Content content = contentFactory.createContent(commonToolWindow.getMainPanel(), "", false);
        // 将被界面工厂代理后创建的 content，添加到工具栏窗口管理器中
        toolWindow.getContentManager().addContent(content);
    }
}
