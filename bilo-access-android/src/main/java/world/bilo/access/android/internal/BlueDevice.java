/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android.internal;


import android.bluetooth.BluetoothDevice;

import world.bilo.access.Device;
import world.bilo.access.PollStream;
import world.bilo.access.State;

public class BlueDevice implements Device {
  private final BluetoothDevice device;
  private final Supervisor blue;

  public BlueDevice(BluetoothDevice device, Supervisor blue) {
    this.device = device;
    this.blue = blue;
  }

  public BluetoothDevice getDevice() {
    return device;
  }

  @Override
  public String getName() {
    return device.getName() + " | " + device.getAddress();
  }

  @Override
  public PollStream stream() {
    return blue;
  }

  @Override
  public State getState() {
    return blue.getState();
  }

}
