package com.lwb.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 卢伟斌.
 * @date 2018/1/11.
 * ==================================
 * Copyright (c) 2018 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class PieChartView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private Paint mPaint;

    private Path mPath;

    private SurfaceHolder mSurfaceHolder;

    private Canvas mCanvas;

    private int width, height;

    private RectF pieRectF, tempRectF;

    private int radius;

    private List<ItemType> itemTypeList, leftTypeList, rightTypeList;

    private List<Point> itemPoints;

    private Thread drawThread;

    private int cell = 0;

    private float innerRadius = 0.0f;

    private float offRadius = 0;

    private int refreshTime = 16;

    private Point firstPoint;

    private int backGroundColor = 0xffffffff;

    private int itemTextSize = 30, textPadding = 8;

    private int defaultStartAngle = -90;

    private boolean isDrawing = false;

    private float pieCell;

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPath = new Path();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        pieRectF = new RectF();
        tempRectF = new RectF();

        setFocusable(true);
        setFocusableInTouchMode(true);

        itemTypeList = new ArrayList<>();
        leftTypeList = new ArrayList<>();
        rightTypeList = new ArrayList<>();
        itemPoints = new ArrayList<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new Thread(this);
        drawThread.start();
        isDrawing = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
        radius = Math.min(width, height) / 4;
        pieRectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        if (itemTypeList == null || itemTypeList.size() == 0) {
            return;
        }
        offRadius = 0f;
        while (offRadius <= 360f + 10) {
            if (!isDrawing) {
                return;
            }
            long start = System.currentTimeMillis();
            offRadius = offRadius + 10;
            drawPie();
            long end = System.currentTimeMillis();
            if (end - start < refreshTime) {
                try {
                    Thread.sleep(refreshTime - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        drawTitle();
    }

    private void drawTitle() {
        mCanvas = mSurfaceHolder.lockCanvas();
        resetPaint();
        float startRadius = defaultStartAngle;
        int count = rightTypeList.size();
        int h;
        if (count > 1) {
            h = (radius * 2) / (count - 1);
        } else {
            h = radius;
        }
        for (int i = 0; i < count; i++) {
            mPath.reset();
            ItemType itemType = rightTypeList.get(i);
            double angle = 2 * Math.PI * ((startRadius + itemType.radius / 2) / 360d);
            int x = (int) (width / 2 + radius * Math.cos(angle));
            int y = (int) (height / 2 + radius * Math.sin(angle));
            Point startPoint = new Point(x, y);
            Point centerPoint = new Point((int) (width / 2 + radius * 1.2f), height / 2 - radius + h * (i));
            Point endPoint = new Point((int) (width * 0.98f), centerPoint.y);
            mPath.moveTo(startPoint.x, startPoint.y);
            mPath.lineTo(centerPoint.x, centerPoint.y);
            mPath.lineTo(endPoint.x, endPoint.y);
            resetPaint();
            mPaint.setStrokeWidth(2);
            mPaint.setColor(itemType.color);
            mPaint.setStyle(Paint.Style.STROKE);
            mCanvas.drawPath(mPath, mPaint);
            startRadius += itemType.radius;
            mPaint.setTextSize(itemTextSize);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mCanvas.drawText(itemType.type, centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                    centerPoint.y - textPadding, mPaint);
            mPaint.setTextSize(itemTextSize * 4 / 5);
            mCanvas.drawText(itemType.getPercent(), centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                    centerPoint.y + (itemTextSize + textPadding) * 4 / 5, mPaint);
        }

        count = leftTypeList.size();
        if (count > 1) {
            h = (radius * 2) / (count - 1);
        } else {
            h = radius;
        }

        for (int i = 0; i < count; i++) {
            mPath.reset();
            ItemType itemType = leftTypeList.get(i);
            double angle = 2 * Math.PI * ((startRadius + itemType.radius / 2) / 360d);
            int x = (int) (width / 2 + radius * Math.cos(angle));
            int y = (int) (height / 2 + radius * Math.sin(angle));
            Point startPoint = new Point(x, y);
            Point centerPoint = new Point((int) (width / 2 - radius * 1.2f), height / 2 - radius + h * (count - 1 - i));
            Point endPoint = new Point((int) (width * 0.02f), centerPoint.y);
            mPath.moveTo(startPoint.x, startPoint.y);
            mPath.lineTo(centerPoint.x, centerPoint.y);
            mPath.lineTo(endPoint.x, endPoint.y);
            resetPaint();
            mPaint.setStrokeWidth(2);
            mPaint.setColor(itemType.color);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mCanvas.drawPath(mPath, mPaint);
            startRadius += itemType.radius;
            mPaint.setTextSize(itemTextSize);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mCanvas.drawText(itemType.type, centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                    centerPoint.y - textPadding, mPaint);
            mPaint.setTextSize(itemTextSize * 4 / 5);
            mCanvas.drawText(itemType.getPercent(), centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                    centerPoint.y + (itemTextSize + textPadding) * 4 / 5, mPaint);
        }

        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    private void drawPie() {
        mCanvas = mSurfaceHolder.lockCanvas();
        if (mCanvas == null) {
            return;
        }
        mCanvas.drawColor(backGroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        int sum = 0;
        for (ItemType itemType : itemTypeList) {
            sum += itemType.widget;
        }
        float a = 360f / sum;
        float startRadius = defaultStartAngle;
        float sumRadius = 0;
        leftTypeList.clear();
        rightTypeList.clear();
        itemPoints.clear();
        for (ItemType itemType : itemTypeList) {
            itemType.radius = itemType.widget * a;
            double al = 2 * Math.PI * ((startRadius + 90) / 360d);
            Point point = new Point((int) (width / 2 + radius * Math.sin(al)),
                    (int) (height / 2 - radius * Math.cos(al)));
            if (cell > 0) {
                if (startRadius == defaultStartAngle) {
                    firstPoint = point;
                }
            }

            double angle = 2 * Math.PI * ((startRadius + itemType.radius / 2) / 360d);
            double sin = -Math.sin(angle);
            double cos = -Math.cos(angle);
            if (cos > 0) {
                leftTypeList.add(itemType);
            } else {
                rightTypeList.add(itemType);
            }
            sumRadius += Math.abs(itemType.radius);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(itemType.color);
            if (pieCell > 0) {
                tempRectF.set(pieRectF.left - (float) (pieCell * cos), pieRectF.top - (float) (pieCell * sin),
                        pieRectF.right - (float) (pieCell * cos), pieRectF.bottom - (float) (pieCell * sin));
                mCanvas.drawArc(tempRectF, startRadius, itemType.radius, true, mPaint);
            } else {
                if (sumRadius <= offRadius) {
                    mCanvas.drawArc(pieRectF, startRadius, itemType.radius, true, mPaint);
                } else {
                    mCanvas.drawArc(pieRectF, startRadius, itemType.radius - (Math.abs(offRadius - sumRadius)), true, mPaint);
                    break;
                }

            }
            startRadius += itemType.radius;
            if (cell > 0 && pieCell == 0) {
                mPaint.setColor(backGroundColor);
                mPaint.setStrokeWidth(cell);
                mCanvas.drawLine(getWidth() / 2, getHeight() / 2, point.x, point.y, mPaint);
            }
        }
        if (cell > 0 && firstPoint != null && pieCell == 0) {
            mPaint.setColor(backGroundColor);
            mPaint.setStrokeWidth(cell);
            mCanvas.drawLine(getWidth() / 2, getHeight() / 2, firstPoint.x, firstPoint.y, mPaint);
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(backGroundColor);
        if (innerRadius > 0 && pieCell == 0) {
            mCanvas.drawCircle(width / 2, height / 2, radius * innerRadius, mPaint);
        }
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    public void resetPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    /**
     * 添加一条分类数据
     *
     * @param itemType
     */
    public void addItemType(ItemType itemType) {
        if (itemTypeList != null) {
            itemTypeList.add(itemType);
        }
    }

    /**
     * 设置每条图之间的间歇大小
     *
     * @param cell
     */
    public void setCell(int cell) {
        this.cell = cell;
    }

    /**
     * 设置内部圆的半径比例，eg：0.5f
     *
     * @param innerRadius
     */
    public void setInnerRadius(float innerRadius) {
        if (innerRadius > 1.0f) {
            innerRadius = 1.0f;
        } else if (innerRadius < 0) {
            innerRadius = 0;
        }
        this.innerRadius = innerRadius;
    }

    /**
     * 设置背景颜色
     *
     * @param backGroundColor
     */
    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    /**
     * 设置每条字体大小
     *
     * @param itemTextSize
     */
    public void setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    /**
     * 设置字体距离横线的padding值
     *
     * @param textPadding
     */
    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

    public void setPieCell(int pieCell) {
        this.pieCell = pieCell;
    }

    public static class ItemType {
        String type;
        int widget;
        int color;
        float radius;
        DecimalFormat df = new DecimalFormat("0.0%");

        public ItemType(String type, int widget, int color) {
            this.type = type;
            this.widget = widget;
            this.color = color;
        }

        public String getPercent() {
            return df.format(radius / 360f);
        }
    }
}

