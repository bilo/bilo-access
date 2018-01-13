/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

public interface ConnectionChangeObserver {

	public void connected();

	public void disconnected();

}