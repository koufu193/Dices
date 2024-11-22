package com.example.kf;

import com.example.kf.widgets.CrossSectionViewWidget;
import com.example.kf.widgets.UndefinedWidget;
import com.example.kf.widgets.ViewWidget;
import imgui.*;
import imgui.app.Application;
import com.example.kf.widgets.ConfigurationWidget;

public class Main extends Application {
    public static ConfigurationWidget configurationWidget =new ConfigurationWidget();
    public static ViewWidget viewWidget =new ViewWidget();
    public static CrossSectionViewWidget crossSectionViewWidget=new CrossSectionViewWidget();
    public static UndefinedWidget undefinedWidget=new UndefinedWidget();
    @Override
    protected void configure(imgui.app.Configuration config) {
        config.setHeight((int)Configuration.height);
        config.setWidth((int)Configuration.width);
        config.setFullScreen(true);
        config.setTitle("");
    }

    @Override
    public void process() {
        setWindowSize();
        configurationWidget.process();
        viewWidget.process();
        crossSectionViewWidget.process();
        undefinedWidget.process();
    }
    private void setWindowSize(){
        ImVec2 size=new ImVec2();
        ImGui.getIO().getDisplaySize(size);
        Configuration.width=size.x;
        Configuration.height=size.y;
    }
    public static void main(String[] args) {
        launch(new Main());
    }

    @Override
    protected void initImGui(imgui.app.Configuration config) {
        super.initImGui(config);
        ImFontAtlas atlas=ImGui.getIO().getFonts();
        short[] ranges={0x20, (short) 0xfffd,0};
        atlas.addFontFromFileTTF("C:\\Windows\\Fonts\\YuGothR.ttc",18,ranges);
        colorBg.set(0,0,0,1);
    }
}
