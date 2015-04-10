package com.example.johnnysung.jkcanvasrender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by johnnysung on 2015/04/10.
 */
public class MyCanvasView extends View {
    private Paint p;
    private Line currentLine;
    private MyCanvasViewDataSource dataSource;

    public MyCanvasView(Context context) {
        super(context);
        init();
    }

    public MyCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 創建畫筆
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.RED);// 設置紅色
        p.setStrokeWidth(5);
    }

    public void setDataSource(MyCanvasViewDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataSource != null) {
            ArrayList<Line> lines = dataSource.getCanvasViewLines();
            if (lines != null) {
                for (Line line : lines) {
                    p.setColor(line.getColor());
                    for (int i = 0; i < line.getPoints().size() - 1; i++) {
                        PointF point1 = line.getPoints().get(i);
                        PointF point2 = line.getPoints().get(i + 1);
                        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, p);// 畫線
                    }
                }
            }
        }
    }
}

interface MyCanvasViewDataSource {
    public ArrayList<Line> getCanvasViewLines();
}
