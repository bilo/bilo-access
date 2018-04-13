/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.List;

interface Stream {
    List<Byte> read();

    void write(List<Byte> data);
}
