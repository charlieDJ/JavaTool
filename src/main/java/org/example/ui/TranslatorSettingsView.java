package org.example.ui;

import javax.swing.*;

public class TranslatorSettingsView {
    private JTextField appId;
    private JTextField secretKey;
    private JPanel main;
    private JPanel settingsPanel;


    public JTextField getAppId() {
        return appId;
    }

    public void setAppId(JTextField appId) {
        this.appId = appId;
    }

    public JTextField getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(JTextField secretKey) {
        this.secretKey = secretKey;
    }

    public JPanel getSettingsPanel() {
        return settingsPanel;
    }

    public void setSettingsPanel(JPanel settingsPanel) {
        this.settingsPanel = settingsPanel;
    }

    public JComponent getComponent() {
        return main;
    }
}
