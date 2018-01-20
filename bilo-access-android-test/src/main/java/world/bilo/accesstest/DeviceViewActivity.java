/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import world.bilo.accesstest.api.Blocks;
import world.bilo.accesstest.bluetooth.Devices;

//TODO handle device rotation

public class DeviceViewActivity extends AppCompatActivity implements DisconnectHandler {
    final private Blocks blocks = new Blocks(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.device_view_activity);

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");

        BluetoothDevice device = Devices.find(address);
        if (device == null) {
            disconnected();
        }

        blocks.connect(device);
    }

    @Override
    protected void onDestroy() {
        blocks.disconnect();
        super.onDestroy();
    }

    @Override
    public void disconnected() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
