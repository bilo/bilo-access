/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

public class ActivityAdapter implements Adapter {
    private final int code;
    private final AdapterListener listener;
    private final Activity activity;

    public ActivityAdapter(int code, AdapterListener listener, Activity activity) {
        this.code = code;
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    public void enable() {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intentBtEnabled, code);
    }

    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            listener.enabled(this);
        }
    }

    @Override
    public Set<BluetoothDevice> devices() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return (adapter == null) ? (new HashSet<BluetoothDevice>()) : adapter.getBondedDevices();
    }
}
