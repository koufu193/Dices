package com.example.kf.widgets;

import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import com.example.kf.Configuration;
import com.example.kf.utils.DiceUtils;
import com.example.kf.Widget;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.glu.Sphere;

public class ViewWidget implements Widget {
    private static final Sphere sphere = new Sphere();

    private float prevMouseX = 0;
    private float prevMouseY = 0;
    private final Quaternionf rotation = new Quaternionf();

    @Override
    public void process() {
        if (Configuration.width - 800 <= 0 || Configuration.height - 300 < 0) return;

        float cubeSize = Configuration.sides * 0.1f;
        float scale = 0.7f / cubeSize * Configuration.scale;

        GL32.glPushMatrix();
        GL32.glViewport(300, 300, (int) Configuration.width - 800, (int) Configuration.height - 300);
        fixAspect(Configuration.width - 800, Configuration.height - 300);

        if (Configuration.hexagon) {
            GL32.glLoadIdentity();
            GL32.glRotatef(-35.25f, 0, 1, 0);
            GL32.glRotatef(45, 1, 0, 0);
        } else {
            adjustRotation();
            rotate(0, 0, 0, rotation);
        }
        GL32.glScalef(scale, scale, scale);
        drawCube(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2, cubeSize);
        GL32.glColor3f(1, 1, 1);
        drawSpheres(0.1f);
        GL32.glPopMatrix();

    }

    private void fixAspect(float width, float height) {
        float aspectRatio = width / height;
        GL32.glMatrixMode(GL32.GL_PROJECTION);
        GL32.glLoadIdentity();

        if (aspectRatio > 1.0f) {
            // 横幅が大きい場合、y軸の描画範囲を調整
            GL32.glOrtho(-1.0f * aspectRatio, 1.0f * aspectRatio, -1.0f, 1.0f, -1.0f, 1.0f);
        } else {
            // 縦長の場合、x軸の描画範囲を調整
            GL32.glOrtho(-1.0f, 1.0f, -1.0f / aspectRatio, 1.0f / aspectRatio, -1.0f, 1.0f);
        }

        GL32.glMatrixMode(GL32.GL_MODELVIEW);
    }

    private void drawCube(float x, float y, float z, float size) {
        GL32.glTranslatef(x, y, z);
        GL32.glBegin(GL32.GL_LINES);
        if (Configuration.withLattices) {
            //格子線付き
            GL32.glColor3f(1, 0, 0);
            for (int i = 0; i <= Configuration.sides; i++) {
                for (int j = 0; j <= Configuration.sides; j++) {
                    GL32.glVertex3f(size / (Configuration.sides) * i, size / (Configuration.sides) * j, 0);
                    GL32.glVertex3f(size / (Configuration.sides) * i, size / (Configuration.sides) * j, size);
                }
            }
            GL32.glColor3f(0, 1, 0);
            for (int i = 0; i <= Configuration.sides; i++) {
                for (int j = 0; j <= Configuration.sides; j++) {
                    GL32.glVertex3f(size / (Configuration.sides) * i, 0, size / (Configuration.sides) * j);
                    GL32.glVertex3f(size / (Configuration.sides) * i, size, size / (Configuration.sides) * j);
                }
            }
            GL32.glColor3f(0, 0, 1);
            for (int i = 0; i <= Configuration.sides; i++) {
                for (int j = 0; j <= Configuration.sides; j++) {
                    GL32.glVertex3f(0, size / Configuration.sides * i, size / (Configuration.sides) * j);
                    GL32.glVertex3f(size, size / Configuration.sides * i, size / (Configuration.sides) * j);
                }
            }
        } else {
            //格子線なし
            GL32.glColor3f(1, 1, 1);
            for (int i = 0; i < 4; i++) {
                float _y = (i & 0b1) != 0 ? size : 0;
                float _z = (i & 0b10) != 0 ? size : 0;
                GL32.glVertex3f(0, _y, _z);
                GL32.glVertex3f(size, _y, _z);
            }
            for (int i = 0; i < 4; i++) {
                float _x = (i & 0b1) != 0 ? size : 0;
                float _z = (i & 0b10) != 0 ? size : 0;
                GL32.glVertex3f(_x, 0, _z);
                GL32.glVertex3f(_x, size, _z);
            }
            for (int i = 0; i < 4; i++) {
                float _x = (i & 0b1) != 0 ? size : 0;
                float _y = (i & 0b10) != 0 ? size : 0;
                GL32.glVertex3f(_x, _y, 0);
                GL32.glVertex3f(_x, _y, size);
            }
        }
        GL32.glEnd();
    }

    private void drawSpheres(float scale) {
        DiceUtils.forEachDiceCombination(Configuration.sum, 4,
                dices ->{
                    float x=(dices[0]+0.5f)*scale;
                    float y=(dices[1]+0.5f)*scale;
                    float z=(dices[2]+0.5f)*scale;
                    boolean overlapped=overlapped(Configuration.sum,dices[0],dices[1],dices[2])&&Configuration.colorOverlapping;
                    if(overlapped) GL32.glColor3f(1,1,0);
                    drawSphere(x,y,z,0.020f);
                    if(overlapped) GL32.glColor3f(1,1,1);
                }
        );
    }
    private boolean overlapped(int sum,int p_1,int p_2,int p_3){
        int min=Math.min(Math.min(p_1,p_2),p_3);
        int max=Math.max(Math.max(p_1,p_2),p_3);
        for(int dec=1;dec<=min;dec++){
            for(int p_4=0;p_4<Configuration.sides;p_4++) {
                if ((1 << (p_1 - dec)) + (1 << (p_2 - dec)) + (1 << (p_3 - dec))+(1<<p_4)==sum){
                    return true;
                }
            }
        }
        for(int inc=1;inc<Configuration.sides-max;inc++){
            for(int p_4=0;p_4<Configuration.sides;p_4++) {
                if ((1 << (p_1 + inc)) + (1 << (p_2 + inc)) + (1 << (p_3 + inc))+(1<<p_4)==sum){
                    return true;
                }
            }
        }
        return false;
    }
    private void drawSphere(float x, float y, float z, float radius) {
        GL32.glPushMatrix();
        GL32.glTranslatef(x, y, z);
        sphere.draw(radius, 16, 16);
        GL32.glPopMatrix();
    }

    private void rotate(float x, float y, float z, Quaternionf rotation) {
        GL32.glTranslatef(x, y, z);
        GL32.glLoadIdentity();
        GL32.glRotatef((float) Math.toDegrees(rotation.angle()), rotation.x, rotation.y, rotation.z);
        GL32.glTranslatef(-x, -y, -z);
    }

    private void adjustRotation() {
        float mouseX = ImGui.getMousePosX();
        float mouseY = ImGui.getMousePosY();
        if (hasToRotate(mouseX, mouseY)) {
            float x_speed = (mouseX - prevMouseX) / 150f;
            float y_speed = (mouseY - prevMouseY) / 150f;
            this.rotation.rotateLocalY(x_speed).rotateLocalX(y_speed);
        } else if (Configuration.autoRotate) {
            rotation.rotateLocalY(0.02f);
        }
        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }

    private boolean hasToRotate(float mouseX, float mouseY) {
        if (mouseX < 0 || mouseY < 0) return false;
        if (mouseX - 300 < 0 || Configuration.width - 500 < mouseX) return false;
        if (Configuration.height - 300 < mouseY) return false;
        if (!ImGui.isMouseDown(ImGuiMouseButton.Left)) return false;
        return true;
    }
}
