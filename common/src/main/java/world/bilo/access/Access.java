/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.Collection;

import world.bilo.util.UniqueOrderedList;

public interface Access {
    public void calc();

    public Collection<? extends Device> getDevices();

    public void connect(Device device);

    public void disconnect();

    public UniqueOrderedList<ConnectionChangeObserver> getConnectionChangeObserver();

    public boolean isConnected();
}