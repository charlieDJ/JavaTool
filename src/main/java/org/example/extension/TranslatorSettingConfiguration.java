package org.example.extension;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import org.example.ui.TranslatorSettingsView;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class TranslatorSettingConfiguration implements Configurable {

    private TranslatorSettingsView view = new TranslatorSettingsView();
    private TranslatorSetting setting = ServiceManager.getService(TranslatorSetting.class).getState();

    @Override
    public String getDisplayName() {
        return "Translator";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return view.getComponent();
    }

    @Override
    public boolean isModified() {
        if (!Objects.equals(setting.getAppID(), view.getAppId().getText())) {
            return true;
        }
        if (!Objects.equals(setting.getSecurityKey(), view.getSecretKey().getText())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() {
        setting.setAppID(view.getAppId().getText());
        setting.setSecurityKey(view.getSecretKey().getText());
    }

}
