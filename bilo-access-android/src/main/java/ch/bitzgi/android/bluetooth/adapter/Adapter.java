/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

public interface Adapter {
    public boolean isEnabled();

    public void enable();

    public Set<BluetoothDevice> devices();
}
