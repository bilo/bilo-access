/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.spp;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public interface Input {

    public void connect(BluetoothDevice device);

    public void disconnect();

    public void send(List<Byte> data);

    public void poll();

}
