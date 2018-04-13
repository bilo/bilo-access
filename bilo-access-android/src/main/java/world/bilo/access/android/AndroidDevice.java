/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android.internal;


import android.bluetooth.BluetoothDevice;

import world.bilo.access.Device;

public class BlueDevice implements Device {
  private final BluetoothDevice device;

  public BlueDevice(BluetoothDevice device) {
    this.device = device;
  }

  public BluetoothDevice getDevice() {
    return device;
  }

  @Override
  public String getName() {
    return device.getName();
  }

  @Override
  public String getAddress() {
    return device.getAddress();
  }

}
