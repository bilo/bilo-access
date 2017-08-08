/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

public class OfflineDevice implements Device {

	@Override
	public String getName() {
		return "<Offline>";
	}

	@Override
	public State getState() {
		return State.Disconnected;
	}

	@Override
	public PollStream stream() {
		return new NullStream();
	}

}
