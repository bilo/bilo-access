/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

public interface Device {
	public String getName();

	public State getState();

	public PollStream stream();

}
