/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

public interface Adapter {
    public void enable();

    public Set<BluetoothDevice> devices();
}
