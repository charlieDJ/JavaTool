package org.example.extension;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.example.ui.DemoWindow;
import org.jetbrains.annotations.NotNull;

/**
 * demo窗口工厂，适合写一些自己用的小工具
 */
public class DemoWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // ContentFactory 在 IntelliJ 平台 SDK 中负责UI界面的管理
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        DemoWindow demoWindow = new DemoWindow();
        // 在界面工厂中创建插件的界面
        Content content = contentFactory.createContent(demoWindow.getMainPanel(), "", false);
        // 将被界面工厂代理后创建的 content，添加到工具栏窗口管理器中
        toolWindow.getContentManager().addContent(content);
    }
}
