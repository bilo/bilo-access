/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.Collection;
import java.util.List;

public interface Devices {
    public void turnOn();

    public void calc();

    public Collection<? extends Device> getDevices();

    public void connect(Device device);

    public void disconnect();

    boolean isConnected();

    public void write(List<Byte> data);

    void setEventHandler(DevicesEventHandler handler);
}