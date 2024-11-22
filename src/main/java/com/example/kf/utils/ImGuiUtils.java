package com.example.kf.utils;

import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import imgui.internal.flag.ImGuiItemFlags;

public final class ImGuiUtils {
    public static boolean button(String s,boolean disabled){
        if(disabled){
            imgui.internal.ImGui.pushItemFlag(ImGuiItemFlags.Disabled,true);
            imgui.internal.ImGui.pushStyleVar(ImGuiStyleVar.Alpha,ImGui.getStyle().getAlpha()*0.5f);
        }
        boolean clicked=ImGui.button(s);
        if(disabled){
            imgui.internal.ImGui.popStyleVar();
            imgui.internal.ImGui.popItemFlag();
        }
        return clicked;
    }
}
