/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import world.bilo.accesstest.api.Blocks;
import world.bilo.accesstest.bluetooth.Devices;

public class SelectDeviceActivity extends AppCompatActivity implements DisconnectHandler {
    private final android.content.Context thisContext = this;
    private ArrayAdapter<String> logAdapter = null;
    final private Blocks blocks = new Blocks(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.connection_log);
        listView.setAdapter(logAdapter);

        Button sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<Byte> data = new ArrayList<>();
                        data.add((byte) 0x80);
                        data.add((byte) 0x81);

                        logOutput("send: " + hexString(data));
                        blocks.send(data);
                    }
                }
        );

        Button disconnectButton = findViewById(R.id.disconnect);
        disconnectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        blocks.disconnect();
                    }
                }
        );

        enableBluetooth();
    }

    private void logOutput(String message) {
        logAdapter.insert(message, 0);
    }

    private void logInput(String message) {
        logAdapter.insert(message, 0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        enableBluetooth();
    }

    private void enableBluetooth() {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        MessageId code = MessageId.REQUEST_ENABLE_BT;

        logOutput("start activity: " + code + ", " + intentBtEnabled);

        startActivityForResult(intentBtEnabled, code.ordinal());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MessageId code = MessageId.values()[requestCode];

        logInput("activity result: " + code + " = " + resultCode);

        switch (code) {
            case REQUEST_ENABLE_BT: {
                if (resultCode == Activity.RESULT_OK) {
                    updateListWithPairedDevices();
                }
                break;
            }
        }
    }

    private void updateListWithPairedDevices() {
        List<DeviceEntry> data = Devices.list();

        ArrayAdapter<DeviceEntry> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, data);

        ListView listView = (ListView) findViewById(R.id.devices_list);
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                DeviceEntry device = (DeviceEntry) parent.getItemAtPosition(position);

                logOutput("connect to " + device.address);

                BluetoothDevice bdevice = Devices.find(device.address);
                if (bdevice == null) {
                    disconnected();
                }

                blocks.connect(bdevice);
            }
        };

        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    @Override
    public void disconnected() {
        logInput("disconnected");
    }

    @Override
    public void connecting(String message) {
        logInput("connecting: " + message);
    }

    @Override
    public void connected() {
        logInput("connected");
    }

    @Override
    public void received(List<Byte> data) {
        logInput("received: " + hexString(data));
    }

    private String hexString(Collection<Byte> data) {
        String result = "";
        boolean first = true;
        for (Byte itr : data) {
            if (first) {
                first = false;
            } else {
                result += " ";
            }
            result += hexString(itr);
        }
        return result;
    }

    private String hexString(Byte value) {
        int upper = (value >> 4) & 0x0f;
        int lower = (value >> 0) & 0x0f;
        return Integer.toHexString(upper) + Integer.toHexString(lower);
    }
}
