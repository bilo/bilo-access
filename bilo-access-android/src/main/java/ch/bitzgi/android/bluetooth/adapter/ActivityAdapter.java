/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
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
    private final Activity activity;

    public ActivityAdapter(int code, Activity activity) {
        this.code = code;
        this.activity = activity;
    }

    @Override
    public boolean isEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    @Override
    public void enable() {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intentBtEnabled, code);
    }

    @Override
    public Set<BluetoothDevice> devices() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return (adapter == null) ? (new HashSet<BluetoothDevice>()) : adapter.getBondedDevices();
    }
}
