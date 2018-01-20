/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.api;

import world.bilo.accesstest.bluetooth.Supervisor;
import world.bilo.stack.stream.Stream;
import java.util.List;

public class BlueSender implements Stream {
    private final Supervisor supervisor;

    public BlueSender(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public void newData(List<Byte> data) {
        supervisor.newData(data);
    }
};
