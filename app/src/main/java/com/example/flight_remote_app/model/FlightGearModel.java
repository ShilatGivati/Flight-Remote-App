package com.example.flight_remote_app.model;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FlightGearModel {

    private final BlockingQueue<Runnable> modelJobs = new LinkedBlockingQueue<>();
    private PrintWriter out;

    public FlightGearModel(String ip, int port) {
        try {
            Socket fg = new Socket(ip, port);
            this.out = new PrintWriter(fg.getOutputStream(), true);

            new Thread(() -> {
                while (true) {
                    try {
                        modelJobs.take().run();
                    } catch (Exception ignored) {
                    }
                }
            }

            ).start();
        } catch (Exception ignored) {
        }
    }

    public void setAileron(double aileron) throws InterruptedException {
        modelJobs.put(
                () -> {
                    out.print("set /controls/flight/aileron " + aileron + "\r\n");
                    out.flush();
                }
        );
    }

    public void setElevator(double elevator) throws InterruptedException {
        modelJobs.put(
                () -> {
                    out.print("set /controls/flight/elevator " + elevator + "\r\n");
                    out.flush();
                }
        );
    }

    public void setRudder(double rudder) throws InterruptedException {
        modelJobs.put(
                () -> {
                    out.print("set /controls/flight/rudder " + rudder + "\r\n");
                    out.flush();
                }
        );
    }

    public void setThrottle(double throttle) throws InterruptedException {
        modelJobs.put(
                () -> {
                    out.print("set /controls/engines/current-engine/throttle " + throttle + "\r\n");
                    out.flush();
                }
        );
    }

}
