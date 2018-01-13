/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo.api;

import world.bilo.stackdemo.bluetooth.Supervisor;
import com.bloctesian.stream.Stream;

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
