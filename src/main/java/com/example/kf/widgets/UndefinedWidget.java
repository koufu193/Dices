package com.example.kf.widgets;

import imgui.ImGui;
import com.example.kf.Configuration;
import com.example.kf.Widget;

import static imgui.flag.ImGuiWindowFlags.*;
import static imgui.flag.ImGuiWindowFlags.NoCollapse;

public class UndefinedWidget implements Widget {
    @Override
    public void process() {
        ImGui.begin("名称未設定",NoResize | NoMove | NoTitleBar | NoCollapse);
        ImGui.setWindowPos(0, Configuration.height-300);
        ImGui.setWindowSize(Configuration.width,300);
        ImGui.end();
    }
}
