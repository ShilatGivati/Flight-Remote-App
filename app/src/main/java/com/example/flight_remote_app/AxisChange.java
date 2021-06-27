package com.example.flight_remote_app;

public interface AxisChange {
    void onChange(double x, double y) throws InterruptedException;
}
