/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import world.bilo.accesstest.bluetooth.Devices;

import java.util.List;

public class SelectDeviceActivity extends AppCompatActivity {
    private final android.content.Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enableBluetooth();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        enableBluetooth();
    }

    private void enableBluetooth() {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intentBtEnabled, MessageId.REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MessageId.REQUEST_ENABLE_BT: {
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
                Intent intent = new Intent(thisContext, DeviceViewActivity.class);
                DeviceEntry device = (DeviceEntry) parent.getItemAtPosition(position);
                intent.putExtra("address", device.address);
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(mMessageClickedHandler);
    }


}
