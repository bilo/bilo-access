/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;


import java.util.List;

import world.bilo.stack.BlockId;

public interface SimpleModel {
    public String name();

    public List<BlockId> blocks();
}
