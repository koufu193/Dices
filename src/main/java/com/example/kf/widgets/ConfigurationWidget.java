package com.example.kf.widgets;

import com.example.kf.Widget;
import com.example.kf.utils.DiceUtils;
import com.example.kf.utils.ImGuiUtils;
import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import com.example.kf.Configuration;
import org.lwjgl.glfw.GLFW;

import static imgui.flag.ImGuiWindowFlags.*;

public class ConfigurationWidget implements Widget {
    private static final ImInt sides = new ImInt(Configuration.sides);
    private static final ImInt imSum = new ImInt(Configuration.sum);
    private static final ImFloat scale = new ImFloat(Configuration.scale);


    @Override
    public void process() {
        ImGui.begin("コンフィグ", NoResize | NoMove | NoTitleBar | NoCollapse);
        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(300, Configuration.height - 300);
        if (ImGui.treeNode("情報")) {
            ImGui.text("和:%d(%s)".formatted(Configuration.sum, Integer.toBinaryString(Configuration.sum)));
            ImGui.text("場合の数:%d".formatted(DiceUtils.calcCombination(Configuration.sum, Configuration.sides, 4)));
            ImGui.treePop();
        }
        if (ImGui.treeNode("一般")) {
            if (ImGui.inputInt("面の数", sides)) {
                if (sides.get() < 1) {
                    sides.set(1);
                }
                Configuration.sides = sides.get();
            }
            if (ImGui.inputInt("和", imSum)) {
                if (imSum.get() < 0) {
                    imSum.set(0);
                }
                Configuration.sum = imSum.get();
            }
            if (ImGuiUtils.button("前", Configuration.sum <= 4) || ImGui.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
                for (int prev = Configuration.sum - 1; 4 <= prev; prev--) {
                    if (DiceUtils.unableToExpress(prev, Configuration.sides, 4)) continue;
                    imSum.set(Configuration.sum = prev);
                    break;
                }
            }
            ImGui.sameLine();
            if (ImGuiUtils.button("次", 4 * DiceUtils.maxRoll(Configuration.sides) <= Configuration.sum) || ImGui.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
                for (int next = Configuration.sum + 1; next <= 4 * DiceUtils.maxRoll(Configuration.sides); next++) {
                    if (DiceUtils.unableToExpress(next, Configuration.sides, 4)) continue;
                    imSum.set(Configuration.sum = next);
                    break;
                }
            }
            ImGui.sameLine();
            if (ImGuiUtils.button("÷2", Configuration.sum <= 4)) {
                imSum.set(Configuration.sum >>>= 1);
            }
            ImGui.sameLine();
            if (ImGuiUtils.button("×2", 4 * DiceUtils.maxRoll(Configuration.sides) <= Configuration.sum)) {
                imSum.set(Configuration.sum <<= 1);
            }
            if (ImGui.sliderScalar("スケール", ImGuiDataType.Float, scale, 1, 2)) {
                Configuration.scale = scale.get();
            }
            Configuration.autoRotate ^= ImGui.checkbox("自動回転", Configuration.autoRotate);
            Configuration.hexagon ^= ImGui.checkbox("六角形型にする", Configuration.hexagon);
            Configuration.withLattices ^= ImGui.checkbox("色付き格子線", Configuration.withLattices);
            Configuration.colorOverlapping ^= ImGui.checkbox("重複を色付け", Configuration.colorOverlapping);
            ImGui.treePop();
        }
        ImGui.end();

    }
}
