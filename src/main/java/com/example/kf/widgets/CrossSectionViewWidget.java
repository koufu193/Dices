package com.example.kf.widgets;

import imgui.ImDrawList;
import imgui.ImGui;
import com.example.kf.Configuration;
import com.example.kf.utils.DiceUtils;
import com.example.kf.Widget;

import static imgui.flag.ImGuiWindowFlags.*;

public class CrossSectionViewWidget implements Widget {
    private static final int WHITE=0xffffffff;
    @Override
    public void process() {

        ImGui.begin("断面図", NoResize | NoMove | NoTitleBar | NoCollapse);
        ImGui.setWindowSize(500, Configuration.height-300);
        ImGui.setWindowPos(Configuration.width - 500, 0);
        ImDrawList drawList=ImGui.getWindowDrawList();

        float circleSize=100f/(Configuration.sides+1);
        for (int i = 0; i < Configuration.sides; i++) {
            float box_x=100 * (i%4)+(Configuration.width - 450);
            float box_y=100 * (i/4)+50;
            drawBox(box_x, box_y, 100);
            DiceUtils.forEachDiceCombination(
                    Configuration.sum-(1<<i),3,
                    dices->{
                        float x=box_x+circleSize*(dices[0]+1);
                        float y=box_y+circleSize*(dices[1]+1);
                        drawList.addCircleFilled(x,y,4f,WHITE);
                    }
            );
        }
        ImGui.end();
    }

    private void drawBox(float x, float y, float size) {
        ImDrawList drawList = ImGui.getWindowDrawList();
        drawList.addLine(x, y, x + size, y, WHITE);
        drawList.addLine(x + size, y, x + size, y + size, WHITE);
        drawList.addLine(x + size, y + size, x, y + size, WHITE);
        drawList.addLine(x, y + size, x, y, WHITE);
    }
}
