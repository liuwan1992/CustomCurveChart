package com.liuwan.curvechart.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.liuwan.curvechart.R;

import java.util.List;

/**
 * Created by liuwan on 2016/9/26.
 */
public class CustomCurveChart extends View {

    // 坐标单位
    private String[] xLabel;
    private String[] yLabel;
    // 曲线数据
    private List<int[]> dataList;
    private List<Integer> colorList;
    private boolean showValue;
    // 默认边距
    private int margin = 20;
    // 距离左边偏移量
    private int marginX = 30;
    // 原点坐标
    private int xPoint;
    private int yPoint;
    // X,Y轴的单位长度
    private int xScale;
    private int yScale;
    // 画笔
    private Paint paintAxes;
    private Paint paintCoordinate;
    private Paint paintTable;
    private Paint paintCurve;
    private Paint paintRectF;
    private Paint paintValue;

    public CustomCurveChart(Context context, String[] xLabel, String[] yLabel,
                            List<int[]> dataList, List<Integer> colorList, boolean showValue) {
        super(context);
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.dataList = dataList;
        this.colorList = colorList;
        this.showValue = showValue;
    }

    public CustomCurveChart(Context context) {
        super(context);
    }

    /**
     * 初始化数据值和画笔
     */
    public void init() {
        xPoint = margin + marginX;
        yPoint = this.getHeight() - margin;
        xScale = (this.getWidth() - 2 * margin - marginX) / (xLabel.length - 1);
        yScale = (this.getHeight() - 2 * margin) / (yLabel.length - 1);

        paintAxes = new Paint();
        paintAxes.setStyle(Paint.Style.STROKE);
        paintAxes.setAntiAlias(true);
        paintAxes.setDither(true);
        paintAxes.setColor(ContextCompat.getColor(getContext(), R.color.color14));
        paintAxes.setStrokeWidth(4);

        paintCoordinate = new Paint();
        paintCoordinate.setStyle(Paint.Style.STROKE);
        paintCoordinate.setDither(true);
        paintCoordinate.setAntiAlias(true);
        paintCoordinate.setColor(ContextCompat.getColor(getContext(), R.color.color14));
        paintCoordinate.setTextSize(15);

        paintTable = new Paint();
        paintTable.setStyle(Paint.Style.STROKE);
        paintTable.setAntiAlias(true);
        paintTable.setDither(true);
        paintTable.setColor(ContextCompat.getColor(getContext(), R.color.color4));
        paintTable.setStrokeWidth(2);

        paintCurve = new Paint();
        paintCurve.setStyle(Paint.Style.STROKE);
        paintCurve.setDither(true);
        paintCurve.setAntiAlias(true);
        paintCurve.setStrokeWidth(3);
        PathEffect pathEffect = new CornerPathEffect(25);
        paintCurve.setPathEffect(pathEffect);

        paintRectF = new Paint();
        paintRectF.setStyle(Paint.Style.FILL);
        paintRectF.setDither(true);
        paintRectF.setAntiAlias(true);
        paintRectF.setStrokeWidth(3);

        paintValue = new Paint();
        paintValue.setStyle(Paint.Style.STROKE);
        paintValue.setAntiAlias(true);
        paintValue.setDither(true);
        paintValue.setColor(ContextCompat.getColor(getContext(), R.color.color1));
        paintValue.setTextAlign(Paint.Align.CENTER);
        paintValue.setTextSize(15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.color1));
        init();
        drawTable(canvas, paintTable);
        drawAxesLine(canvas, paintAxes);
        drawCoordinate(canvas, paintCoordinate);
        for (int i = 0; i < dataList.size(); i++) {
            drawCurve(canvas, paintCurve, dataList.get(i), colorList.get(i));
            if (showValue) {
                drawValue(canvas, paintRectF, dataList.get(i), colorList.get(i));
            }
        }
    }

    /**
     * 绘制坐标轴
     */
    private void drawAxesLine(Canvas canvas, Paint paint) {
        // X
        canvas.drawLine(xPoint, yPoint, this.getWidth() - margin / 6, yPoint, paint);
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint - margin / 3, paint);
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint + margin / 3, paint);

        // Y
        canvas.drawLine(xPoint, yPoint, xPoint, margin / 6, paint);
        canvas.drawLine(xPoint, margin / 6, xPoint - margin / 3, margin / 2, paint);
        canvas.drawLine(xPoint, margin / 6, xPoint + margin / 3, margin / 2, paint);
    }

    /**
     * 绘制表格
     */
    private void drawTable(Canvas canvas, Paint paint) {
        Path path = new Path();
        // 横向线
        for (int i = 1; (yPoint - i * yScale) >= margin; i++) {
            int startX = xPoint;
            int startY = yPoint - i * yScale;
            int stopX = xPoint + (xLabel.length - 1) * xScale;
            path.moveTo(startX, startY);
            path.lineTo(stopX, startY);
            canvas.drawPath(path, paint);
        }

        // 纵向线
        for (int i = 1; i * xScale <= (this.getWidth() - margin); i++) {
            int startX = xPoint + i * xScale;
            int startY = yPoint;
            int stopY = yPoint - (yLabel.length - 1) * yScale;
            path.moveTo(startX, startY);
            path.lineTo(startX, stopY);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 绘制刻度
     */
    private void drawCoordinate(Canvas canvas, Paint paint) {
        // X轴坐标
        for (int i = 0; i <= (xLabel.length - 1); i++) {
            paint.setTextAlign(Paint.Align.CENTER);
            int startX = xPoint + i * xScale;
            canvas.drawText(xLabel[i], startX, this.getHeight() - margin / 6, paint);
        }

        // Y轴坐标
        for (int i = 0; i <= (yLabel.length - 1); i++) {
            paint.setTextAlign(Paint.Align.LEFT);
            int startY = yPoint - i * yScale;
            int offsetX;
            switch (yLabel[i].length()) {
                case 1:
                    offsetX = 28;
                    break;

                case 2:
                    offsetX = 20;
                    break;

                case 3:
                    offsetX = 12;
                    break;

                case 4:
                    offsetX = 5;
                    break;

                default:
                    offsetX = 0;
                    break;
            }
            int offsetY;
            if (i == 0) {
                offsetY = 0;
            } else {
                offsetY = margin / 5;
            }
            // x默认是字符串的左边在屏幕的位置，y默认是字符串是字符串的baseline在屏幕上的位置
            canvas.drawText(yLabel[i], margin / 4 + offsetX, startY + offsetY, paint);
        }
    }

    /**
     * 绘制曲线
     */
    private void drawCurve(Canvas canvas, Paint paint, int[] data, int color) {
        paint.setColor(ContextCompat.getColor(getContext(), color));
        Path path = new Path();
        for (int i = 0; i <= (xLabel.length - 1); i++) {
            if (i == 0) {
                path.moveTo(xPoint, toY(data[0]));
            } else {
                path.lineTo(xPoint + i * xScale, toY(data[i]));
            }

            if (i == xLabel.length - 1) {
                path.lineTo(xPoint + i * xScale, toY(data[i]));
            }
        }
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制数值
     */
    private void drawValue(Canvas canvas, Paint paint, int data[], int color) {
        paint.setColor(ContextCompat.getColor(getContext(), color));
        for (int i = 1; i <= (xLabel.length - 1); i++) {
            RectF rect;
            if (toY(data[i - 1]) < toY(data[i])) {
                rect = new RectF(xPoint + i * xScale - 20, toY(data[i]) - 15,
                        xPoint + i * xScale + 20, toY(data[i]) + 5);
                canvas.drawRoundRect(rect, 5, 5, paint);
                canvas.drawText(data[i] + "w", xPoint + i * xScale, toY(data[i]), paintValue);
            } else if (toY(data[i - 1]) > toY(data[i])) {
                rect = new RectF(xPoint + i * xScale - 20, toY(data[i]) - 5,
                        xPoint + i * xScale + 20, toY(data[i]) + 15);
                canvas.drawRoundRect(rect, 5, 5, paint);
                canvas.drawText(data[i] + "w", xPoint + i * xScale, toY(data[i]) + 10, paintValue);
            } else {
                rect = new RectF(xPoint + i * xScale - 20, toY(data[i]) - 10,
                        xPoint + i * xScale + 20, toY(data[i]) + 10);
                canvas.drawRoundRect(rect, 5, 5, paint);
                canvas.drawText(data[i] + "w", xPoint + i * xScale, toY(data[i]) + 5, paintValue);
            }
        }
    }

    /**
     * 数据按比例转坐标
     */
    private float toY(int num) {
        float y;
        try {
            float a = (float) num / 100.0f;
            y = yPoint - a * yScale;
        } catch (Exception e) {
            return 0;
        }
        return y;
    }

}
