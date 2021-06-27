package com.example.flight_remote_app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.flight_remote_app.AxisChange;

public class JoystickView extends View {

    private Paint paint; //To draw the joystick circles.
    private static final String[] backgroundColors = {
            "#3b3f42", "#393d40", "#383b3e", "#36393c", "#34383a",
            "#333638", "#313436", "#303234", "#2e3032", "#2c2e30",
            "#2b2d2e", "#292b2c", "#28292a", "#262728", "#252626",
            "#232424", "#222223", "#202021", "#1f1f1f", "#1d1d1d"};
    //Bounds
    private int bigCircleX;
    private int bigCircleY;
    private double bigCircleRadius;
    //Joystick
    private int smallCircleX;
    private int smallCircleY;
    private double smallCircleRadius;

    public AxisChange onChange;

    public JoystickView(Context context) {
        super(context);
        this.paint = new Paint();
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.paint = new Paint();
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.smallCircleRadius = 0.15 * Math.min(w, h);
        this.smallCircleX = w / 2;
        this.smallCircleY = h / 2;
        this.bigCircleRadius = 0.4 * Math.min(w, h);
        this.bigCircleX = w / 2;
        this.bigCircleY = h / 2;
        Log.i("Circle", "" + this.bigCircleRadius);
    }

    private double getAngle(int x, int y) {
        if (this.bigCircleY == y) {
            if (x < this.bigCircleX) {
                return 180;
            } else {
                return 0;
            }
        }
        double incline = (double)(this.bigCircleY - y) / (double)(this.bigCircleX - x);
        double deg = Math.toDegrees(Math.atan(Math.abs(incline)));
        if (x < this.bigCircleX && y > this.bigCircleY) {
            return 180 - deg;
        }
        if (x < this.bigCircleX && y < this.bigCircleY) {
            return 180 + deg;
        }
        if (x > this.bigCircleX && y < this.bigCircleY) {
            return 360 - deg;
        }
        return deg;
    }

    //(y - y1 + mx1)/m = x
    private int getPositionX(int touchX, int touchY, int circleY, boolean isPositive) {
        if (touchX == this.bigCircleX) {
            return touchX;
        }
        double incline = (double)(touchY - this.bigCircleY) / (double)(touchX - this.bigCircleX);
        if (incline == 0) {
            if (isPositive) {
                return (int)(this.bigCircleX + this.bigCircleRadius);
            } else {
                return (int)(this.bigCircleX - this.bigCircleRadius);
            }
        }
        return (int)((circleY - touchY + incline * touchX) / incline);
    }

    private void onMove(int x, int y) {
        double dis = Math.sqrt(Math.pow(x - this.bigCircleX, 2) + Math.pow(y - this.bigCircleY, 2));
        if (dis <= this.bigCircleRadius) {
            this.smallCircleX = x;
            this.smallCircleY = y;
        } else  {
            if (x == this.bigCircleX) {
                if (y > this.bigCircleY) {
                    this.smallCircleY = this.bigCircleY + (int) this.bigCircleRadius;
                } else {
                    this.smallCircleY = this.bigCircleY - (int) this.bigCircleRadius;
                }
                this.smallCircleX = this.bigCircleX;
            } else {
                double angle = getAngle(x, y);
                if (angle <= 90) {
                    this.smallCircleY = (int)(this.bigCircleY + this.bigCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.smallCircleX = getPositionX(x, y, this.smallCircleY, true);
                } else if (angle <= 180) {
                    this.smallCircleY = (int)(this.bigCircleY + this.bigCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.smallCircleX = getPositionX(x, y, this.smallCircleY, false);
                } else if (angle <= 270) {
                    this.smallCircleY = (int) (this.bigCircleY - this.bigCircleRadius * Math.sin(Math.toRadians(angle - 180)));
                    this.smallCircleX = getPositionX(x, y, this.smallCircleY, false);
                } else {
                    this.smallCircleY = (int) (this.bigCircleY - this.bigCircleRadius * Math.sin(Math.toRadians(360 - angle)));
                    this.smallCircleX = getPositionX(x, y, this.smallCircleY, true);
                }
            }
        }

        try {
            double a = (this.smallCircleX - this.bigCircleX) / (this.bigCircleRadius);
            double e = (this.smallCircleY - this.bigCircleY) / (this.bigCircleRadius);
            e = -1 * e;
            onChange.onChange(a, e);
        } catch (Exception ignored) {

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_MOVE:
                onMove(touchX, touchY);
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                this.smallCircleX = this.bigCircleX;
                this.smallCircleY = this.bigCircleY;
                try {
                    onChange.onChange(0, 0);
                } catch (Exception ignored) {

                }
                this.invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.parseColor("#1D1D1D"));
        canvas.drawCircle(this.bigCircleX, this.bigCircleY, (float)this.bigCircleRadius, paint);


        for (int i = 0; i < 20; i++) {
            paint.setColor(Color.parseColor(backgroundColors[i]));
            canvas.drawCircle(this.bigCircleX, this.bigCircleY, (float)this.bigCircleRadius - (i * 25), paint);
        }


        paint.setColor(Color.parseColor("#ff007f"));
        canvas.drawCircle(this.smallCircleX, this.smallCircleY, (float)this.smallCircleRadius, paint);
    }
}
