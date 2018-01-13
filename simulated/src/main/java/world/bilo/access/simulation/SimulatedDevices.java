/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.List;

import world.bilo.access.Device;
import world.bilo.access.Devices;
import world.bilo.access.OfflineDevice;
import world.bilo.stack.support.Time;

//TODO add tests
public class SimulatedDevices implements Devices {
  private final SimpleModels models;
  private final Time time;
  private Device device;

  public SimulatedDevices(SimpleModels models, Time time) {
    disconnect();
    this.models = models;
    this.time = time;
  }

  @Override
  public void disconnect() {
    device = new OfflineDevice();
  }

  @Override
  public List<SimulatedDevice> getDevices() {
    return new SimulatedDevicesList(models, time);
  }

  @Override
  public Device getConnectedDevice() {
    return device;
  }

  @Override
  public void connect(Device device) {
    this.device = device;
  }

}
