/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android.internal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import world.bilo.access.Device;
import world.bilo.access.Devices;
import world.bilo.access.OfflineDevice;

public class BlueDevices implements Devices {
  private Device connected = new OfflineDevice();
  private final Supervisor blueCom;

  public BlueDevices(Supervisor blueCom) {
    this.blueCom = blueCom;
  }

  @Override
  public void disconnect() {
    blueCom.disconnect();
  }

  @Override
  public List<Device> getDevices() {
    List<Device> devices = new ArrayList<Device>();
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    if (adapter != null) {
      for (BluetoothDevice device : adapter.getBondedDevices()) {
        devices.add(new BlueDevice(device, blueCom));
      }
    }
    return devices;
  }

  @Override
  public void connect(Device device) {
    connected = device;
    blueCom.connect(((BlueDevice) device).getDevice());
  }

  @Override
  public Device getConnectedDevice() {
    return connected;
  }

}
