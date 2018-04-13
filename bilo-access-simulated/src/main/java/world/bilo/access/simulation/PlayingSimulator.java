/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.ArrayList;
import java.util.List;

import world.bilo.stack.support.Time;

import static world.bilo.stack.stream.message.ByteConvertion.toByte;

class PlayingSimulator implements Stream {
    private final Time time;
    private long lastSent = 0;
    private boolean pendingRequest = false;
    private final List<List<Byte>> data;

    public PlayingSimulator(Time time, List<List<Byte>> data) {
        this.time = time;
        this.data = data;
    }

    @Override
    public List<Byte> read() {
        if (!pendingRequest) {
            return new ArrayList<>();
        }

        long now = time.getTimeMs() / 1000;

        if (lastSent == now) {
            return new ArrayList<>();
        }
        lastSent = now;
        pendingRequest = false;

        int step = (int) (now % (2 * data.size()));

        ArrayList<Byte> oneBlock = new ArrayList<>();
        oneBlock.add(toByte(0x80));

        if (step <= data.size()) {
            for (int i = 0; i < step; i++) {
                oneBlock.addAll(data.get(i));
            }
        } else {
            int start = step - data.size();
            for (int i = start; i < data.size(); i++) {
                oneBlock.addAll(data.get(i));
            }
        }

        oneBlock.add(toByte(0x81));

        return oneBlock;
    }

    @Override
    public void write(List<Byte> data) {
        pendingRequest = true;
    }


}
