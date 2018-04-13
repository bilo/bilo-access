/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.accesstest;

import world.bilo.access.Device;

class GuiDevice {
    public final Device device;

    GuiDevice(Device device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return device.getName() + "\n" + device.getAddress();
    }
}
