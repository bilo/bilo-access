/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.List;

import world.bilo.access.Devices;
import world.bilo.access.ConnectionChangeObserver;
import world.bilo.access.Device;
import world.bilo.stack.support.Time;
import world.bilo.util.UniqueOrderedList;

//TODO add tests
public class SimulatedAccess implements Devices {
  private final SimpleModels models;
  private final Time time;
  private Device device = null;

  public SimulatedAccess(SimpleModels models, Time time) {
    disconnect();
    this.models = models;
    this.time = time;
  }

  @Override
  public void disconnect() {
    device = null;
    //TODO notify
  }

  @Override
  public UniqueOrderedList<ConnectionChangeObserver> getConnectionChangeObserver() {
    //TODO implement
    return null;
  }

  @Override
  public boolean isConnected() {
    return device != null;
  }

  @Override
  public void calc() {
  }

  @Override
  public List<SimulatedDevice> getDevices() {
    return new SimulatedDevicesList(models, time);
  }

  @Override
  public void connect(Device device) {
    this.device = device;
    //TODO notify
  }

}
