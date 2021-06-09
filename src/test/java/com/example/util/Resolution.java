package com.example.util;

import org.openqa.selenium.Dimension;

public enum Resolution {

    _1920x1080(1920, 1080),

    _1366x768(1366, 768);

    private final int width, height;

    Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Dimension toDimension(){
        return new Dimension(width, height);
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
