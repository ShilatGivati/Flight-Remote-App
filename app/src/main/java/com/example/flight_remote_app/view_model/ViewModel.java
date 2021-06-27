package com.example.flight_remote_app.view_model;

import com.example.flight_remote_app.model.FlightGearModel;

public class ViewModel {

    private final FlightGearModel model;

    public ViewModel(String ip, int port) {
        this.model = new FlightGearModel(ip, port);
    }

    private double convertThrottle(int value) {
        int oldRange = 100;
        int newRange = 1;
        return (double)((double)(value * newRange) / oldRange);
    }

    private double convertRudder(int value) {
        int oldRange = 100;
        int newRange = 2;
        return (double)((double)(value * newRange) / oldRange) - 1;
    }

    public void setRudder(int rudder) throws InterruptedException {
        this.model.setRudder(convertRudder(rudder));
    }

    public void setThrottle(int throttle) throws InterruptedException {
        this.model.setThrottle(convertThrottle(throttle));
    }

    public void setAileron(double a) throws InterruptedException {
        this.model.setAileron(a);
    }

    public void setElevator(double e) throws InterruptedException {
        this.model.setElevator(e);
    }

}

