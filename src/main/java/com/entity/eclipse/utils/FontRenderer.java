package com.entity.eclipse.utils;

import com.entity.eclipse.Eclipse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class FontRenderer {
    private static class FontInfo {
        protected int texID;
        protected int[] xPos;
        protected int[] yPos;
        protected int startChar;
        protected int endChar;
        protected FontMetrics metrics;

        public FontInfo(int size, int startChar, int endChar) {
            this.startChar = startChar;
            this.endChar = endChar;

            BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = img.getGraphics();
        }
    }

    public static FontInfo DEFAULT = loadFont("Nunito");
    public static FontInfo MONOSPACE = loadFont("JetBrains Mono");

    public static FontInfo loadFont(String name) {
        InputStream stream = FontRenderer.class.getResourceAsStream("/assets/" + Eclipse.MOD_ID + "/fonts/" + name + ".ttf");

        return new FontInfo(0, 0, 0);
    }
}
