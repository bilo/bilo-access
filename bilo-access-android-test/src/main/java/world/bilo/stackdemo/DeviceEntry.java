/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo;

public class DeviceEntry {
    final public String name;
    final public String address;

    public DeviceEntry(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return name + '\n' + address;
    }
}
