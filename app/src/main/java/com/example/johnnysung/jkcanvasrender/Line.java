package com.example.johnnysung.jkcanvasrender;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by johnnysung on 2015/04/10.
 */
public class Line {
    private int color;
    private ArrayList<PointF> points;

    public Line() {
        points = new ArrayList<>();
    }

    public Line(int color) {
        this.color = color;
        points = new ArrayList<>();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ArrayList<PointF> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PointF> points) {
        this.points = points;
    }

    public void addPoint(PointF pointF) {
        points.add(pointF);
    }
}
