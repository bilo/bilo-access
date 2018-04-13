/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.ArrayList;
import java.util.List;

class NullStream implements Stream {
    @Override
    public List<Byte> read() {
        return new ArrayList<>();
    }

    @Override
    public void write(List<Byte> data) {
    }
}
