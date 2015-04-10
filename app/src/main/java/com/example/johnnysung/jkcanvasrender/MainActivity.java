package com.example.johnnysung.jkcanvasrender;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener, MyCanvasViewDataSource {

    private static final String TAG = "MainActivity";
    @InjectView(R.id.color_palette_ll)
    LinearLayout color_palette_ll;

    @InjectView(R.id.canvas_view)
    MyCanvasView canvas_view;

    private ArrayList<Line> lines;
    private int currentColor = Color.BLUE;
    private PointF lastPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        lines = new ArrayList<>();

        canvas_view.setOnTouchListener(this);
        canvas_view.setDataSource(this);
        initColorPalette();
    }

    private void initColorPalette() {
        for (int i = 0; i < color_palette_ll.getChildCount(); i++) {
            if (color_palette_ll.getChildAt(i) instanceof ViewGroup) {
                ViewGroup parentView = (ViewGroup) color_palette_ll.getChildAt(i);
                for (int j = 0; j < parentView.getChildCount(); j++) {
                    View view = parentView.getChildAt(j);
                    if (view.getTag() != null) {
                        String colorStr = String.valueOf(view.getTag());
                        view.setBackgroundColor(Color.parseColor(colorStr));
                        view.setOnClickListener(this);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bitmap bitmap;
        String filename;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sb = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        switch (id) {
            case R.id.action_clear:
                lines.clear();
                canvas_view.invalidate();
                break;
            case R.id.action_save_jpg:
                try {
                    filename = String.format("%s.jpg", sb.format(cal.getTime()));
                    bitmap = ViewUtils.getViewBitmap(canvas_view, Color.WHITE);
                    BitmapUtils.saveAsJPG(this, bitmap, filename);

                    canvas_view.invalidate();
                    canvas_view.setOnTouchListener(this);
                    Toast.makeText(this, String.format("Image saved as: %s", filename), Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_save_png:
                try {
                    filename = String.format("%s.png", sb.format(cal.getTime()));

                    bitmap = ViewUtils.getViewBitmap(canvas_view, Color.TRANSPARENT);
                    BitmapUtils.saveAsPNG(this, bitmap, filename);

                    canvas_view.invalidate();
                    canvas_view.setOnTouchListener(this);
                    Toast.makeText(this, String.format("Image saved as: %s", filename), Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            String colorStr = String.valueOf(v.getTag());
            currentColor = Color.parseColor(colorStr);
            Toast.makeText(this, "Selected " + colorStr, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ArrayList<Line> getCanvasViewLines() {
        return lines;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        String act = "";
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                act = "DOWN";
                lines.add(new Line(currentColor));
                lastPoint = new PointF(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                act = "MOVE";
                if (!(Math.abs(lastPoint.x - x) > 3 || Math.abs(lastPoint.y - y) > 3)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                act = "UP";
                break;
        }

        Line currentLine = lines.get(lines.size() - 1);
        currentLine.addPoint(new PointF(x, y));

        Log.v(TAG, "action = " + act + " x= " + x + " y= " + y);
        canvas_view.invalidate();

        return true;
    }
}
