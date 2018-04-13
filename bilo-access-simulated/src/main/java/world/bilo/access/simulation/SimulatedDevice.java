/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import world.bilo.access.Device;

class SimulatedDevice implements Device {
    private final SimpleModel model;


    public SimulatedDevice(SimpleModel model) {
        this.model = model;
    }

    @Override
    public String getName() {
        return "Simulated: " + model.name();
    }

    @Override
    public String getAddress() {
        return "00:00:00:00:00:00";
    }

    public SimpleModel getModel() {
        return model;
    }
}
