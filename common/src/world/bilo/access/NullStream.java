/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.ArrayList;
import java.util.List;

public class NullStream implements PollStream {

	@Override
	public List<Byte> read() {
		return new ArrayList<>();
	}

	@Override
	public void newData(List<Byte> data) {
	}

}
