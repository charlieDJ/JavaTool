package org.example.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.example.ui.CommonToolWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FormatJsonListener extends AbstractAction {

    private final CommonToolWindow window;

    public FormatJsonListener(CommonToolWindow window) {
        this.window = window;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String pretty;
        try {
            String jsonText = window.getJsonArea().getText();
            JSONObject object = JSON.parseObject(jsonText);
            pretty = JSON.toJSONString(object, JSONWriter.Feature.PrettyFormat);
        } catch (Exception e) {
            pretty = e.getMessage();
        }
        window.getFormattedJsonArea().setText(pretty);
    }
}
