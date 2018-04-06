/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android;


import android.bluetooth.BluetoothDevice;

import world.bilo.access.Device;

public class AndroidDevice implements Device {
  private final BluetoothDevice device;

  public AndroidDevice(BluetoothDevice device) {
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
