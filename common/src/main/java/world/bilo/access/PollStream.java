/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.List;

import world.bilo.stack.stream.Stream;

public interface PollStream extends Stream {
	public List<Byte> read();

}
