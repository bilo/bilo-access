/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.Collection;

public interface Devices {
	public void connect(Device device);

	public void disconnect();

	public Device getConnectedDevice();

	public Collection<? extends Device> getDevices();

}
