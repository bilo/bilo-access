/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

public interface Device {
	public String getName();

	//TODO add getAddress

	//TODO replace with events
	@Deprecated
	public State getState();

	//TODO move to Devices?
	public PollStream stream();

}
