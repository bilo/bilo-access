/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth.event.supervisor;

public interface Event {
    public void accept(Visitor visitor);
}

