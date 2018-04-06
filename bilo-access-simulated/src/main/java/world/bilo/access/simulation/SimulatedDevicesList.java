/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.ArrayList;

import world.bilo.stack.support.Time;

public class SimulatedDevicesList extends ArrayList<SimulatedDevice> {
  private static final long serialVersionUID = 5266338942115268587L;

  public SimulatedDevicesList(SimpleModels models, Time time) {
    for (SimpleModel model : models.getModels()) {
      add(new SimulatedDevice(model, time));
    }
  }
}
