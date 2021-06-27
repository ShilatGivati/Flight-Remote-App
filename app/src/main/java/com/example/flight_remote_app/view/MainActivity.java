package com.example.flight_remote_app.view;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.flight_remote_app.R;
import com.example.flight_remote_app.view_model.ViewModel;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText ipEditText;
    private EditText portEditText;
    private VerticalScroll throttleScroll;
    private SeekBar rudderScroll;

    private ViewModel viewModel;

    private JoystickView joystickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.ipEditText = (EditText) findViewById(R.id.inputIp);
        this.portEditText = (EditText) findViewById(R.id.inputPort);
        AppCompatButton connectButton = (AppCompatButton) findViewById(R.id.connectButton);
        this.throttleScroll = (VerticalScroll) findViewById(R.id.controller_throttle);
        this.rudderScroll = (SeekBar) findViewById(R.id.controller_rudder);
        this.joystickView = (JoystickView) findViewById(R.id.joystick);

        connectButton.setOnClickListener(
                v -> connectToViewModel()
        );

        setSeekbarListeners();
    }

    private void setSeekbarListeners() {
        this.rudderScroll.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        try {
                            if (viewModel != null)
                                viewModel.setRudder(progress);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        this.throttleScroll.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        try {
                            if (viewModel != null)
                                viewModel.setThrottle(progress);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );
    }

    private void showSeekbarError(String errorMessage) {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        Snackbar snackbar = Snackbar.make(mainLayout, errorMessage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void connectToViewModel() {
        if (this.ipEditText.getText().toString().isEmpty() ||
                this.portEditText.getText().toString().isEmpty()) {
            showSeekbarError("Please enter both IP and port number");
            return;
        }
        String[] ip = this.ipEditText.getText().toString().split("\\.");
        String port = this.portEditText.getText().toString();

        try {
            int portInt = Integer.parseInt(port);
            if (portInt > 65535 || portInt < 0) {
                showSeekbarError("Port out of range.");
                return;
            }

            if (ip.length != 4) {
                showSeekbarError("Wrong IP format");
                return;
            }
            for (int i = 0; i < 4; i++) {
                int ipPart = Integer.parseInt(ip[i]);
                if (ipPart < 0 || ipPart > 255) {
                    showSeekbarError("IP out of range");
                    return;
                }
            }
        } catch (Exception e) {
            showSeekbarError("Wrong IP or port format.");
            return;
        }
        this.viewModel = new ViewModel(this.ipEditText.getText().toString(), Integer.parseInt(port));
        this.joystickView.onChange = (a, e) -> {
            this.viewModel.setAileron(a);
            this.viewModel.setElevator(e);
        };
    }
}