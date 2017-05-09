package xstar.com.direction;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xstar on 2016-12-01.
 */
public class DirectionView extends View {
    public static final String TAG = "DirectionView";
    private static final String[] directs = {"北", "东", "南", "西"};

    public DirectionView(Context context) {
        this(context, null);
    }

    public DirectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private DisplayMetrics displayMetrics = new DisplayMetrics();

    public DirectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        textSize = (int) (displayMetrics.scaledDensity * textSize);
    }

    Paint paint = new Paint();
    private String text = getDirectDescription(0);
    private int scale_len = 35;
    private int directAngle = 0;
    private int space = 8;
    private int textSize = 25;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas, scale_len);
        drawLittleTriangle(canvas, scale_len);
        drawDirectText(canvas, directAngle, text);
    }

    private void drawCircle(Canvas canvas, int len) {
        initClockPaint();
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int half_w = width >> 1;
        int half_h = height >> 1;
        float radius = half_w - 20;//圆盘的半径
        for (int i = 0; i < 360; i++) {
            //画刻度之前，先把画布的状态保存下来
            canvas.save();
            //让画布旋转3/5度，参数一是需要旋转的度数，参数2,3是旋转的圆心
            canvas.rotate(i, half_w, half_h);
            //旋转后再圆上画上一长10dp的刻度线
            canvas.drawLine(half_w, half_h - radius, half_w, half_h - radius + len, paint);
            //恢复画布
            canvas.restore();
        }
    }

    private void initClockPaint() {
        paint.setAntiAlias(true);//消除锯齿
        paint.setColor(Color.WHITE);//设置圆盘画笔的颜色为红色
        paint.setStyle(Paint.Style.STROKE);//设置画笔的类型为描边
        paint.setStrokeWidth(1);//设置描边宽度
        paint.setAlpha(100);//设置画笔透明度，最高值为255
    }

    private void drawLittleTriangle(Canvas canvas, int triangle_len) {
        paint.setStyle(Paint.Style.FILL);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int half_w = width >> 1;
        int half_h = height >> 1;
        int tl_half = triangle_len >> 1;
        int tl_height = (int) (tl_half * Math.pow(3, 0.5));
        int radius = computeCircle(canvas, 1);
        int start_y = half_h - radius - tl_height - space;
        Path path = new Path();
        path.moveTo(half_w, start_y);
        path.lineTo(half_w - tl_half, start_y + tl_height);
        path.lineTo(half_w + tl_half, start_y + tl_height);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawDirectText(Canvas canvas, int angle, String text) {
        paint.setStyle(Paint.Style.FILL);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int half_w = width >> 1;
        int half_h = height >> 1;

        int radius = computeCircle(canvas, 3);
        for (int i = 0; i < 4; i++) {
            canvas.save();
            canvas.rotate(i * 90 + angle, half_w, half_h);
            canvas.drawText(directs[i], 0, 1, half_w - (textSize / 2), half_h - radius + textSize, paint);
            canvas.restore();
        }

        Rect textR = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), textR);
        canvas.drawText(text, 0, text.length(), half_w - (textR.width() / 2), half_h - (textR.height() / 2), paint);
        half_h += textR.height();
//        drawAngleInfo(directAngle, canvas, half_w, half_h);
    }

    private void drawAngleInfo(int angle, Canvas canvas, int cx, int cy) {
        Rect textR = new Rect();
        while (angle > 90) angle = angle - 90;
        text = angle + "度";
        paint.setTextSize((float) (textSize * 0.82));
        paint.getTextBounds(text, 0, text.length(), textR);
        canvas.drawText(text, 0, text.length(), cx - (textR.width() / 2), cy - (textR.height() / 2), paint);
    }

    public int getScale_len() {
        return scale_len;
    }

    public void setScale_len(int scale_len) {
        this.scale_len = scale_len;
        invalidate();
    }

    public int getDirectAngle() {
        return directAngle;
    }

    public void setDirectAngle(int directAngle) {
        this.directAngle = directAngle;
        this.text = getDirectDescription(directAngle);
        invalidate();
    }

    private String getDirectDescription(int directAngle) {
        String text;
        if (inRange(directAngle, 0, 10) && inRange(directAngle, 350, 360)) text = "正北";
        else if (inRange(directAngle, 10, 80)) text = "东偏北";
        else if (inRange(directAngle, 80, 100)) text = "正东";
        else if (inRange(directAngle, 100, 170)) text = "东偏南";
        else if (inRange(directAngle, 170, 190)) text = "正南";
        else if (inRange(directAngle, 190, 260)) text = "西偏南";
        else if (inRange(directAngle, 260, 280)) text = "正西";
        else if (inRange(directAngle, 280, 350)) text = "西偏北";
        else text = "未知方向";
        return text;
    }

    private boolean inRange(int num, int x, int y) {
        if (x > y) {
            int temp = x;
            x = y;
            y = temp;
        }
        return x <= num && y >= num;
    }

    private int computeCircle(Canvas canvas, int floor) {
        int width = canvas.getWidth();
        int half_w = width >> 1;
        int radius = 0;
        switch (floor) {
            case 1:
                radius = half_w - 20;//圆盘的半径
                break;
            case 2:
                radius = computeCircle(canvas, 1);
//                radius -= (scale_len + space);
                break;
            case 3:
                radius = computeCircle(canvas, 2);
                radius -= ((scale_len / 2 * Math.pow(3, 0.5)) + space);
                break;
            case 4:
                radius = computeCircle(canvas, 3);
                radius -= textSize + space;
                break;

        }
        return radius;
    }
}
