/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import world.bilo.stack.support.Time;

public class Bilo_Connection_Test {
	private final PollStream stream = mock(PollStream.class);
	private final Device device = mock(Device.class);
	private final Devices devices = mock(Devices.class);
	private final Time time = mock(Time.class);
	private final Bilo testee = new Bilo(devices, time);
	private final List<Byte> DefaultMessage = new ArrayList<>();
	private final List<Byte> MinimalMessage = new ArrayList<>();

	@Before
	public void fill_message() {
		DefaultMessage.add((byte) 0x80);
		DefaultMessage.add((byte) 0x00);
		DefaultMessage.add((byte) 0x00);
		DefaultMessage.add((byte) 0x81);

		MinimalMessage.add((byte) 0x80);
		MinimalMessage.add((byte) 0x81);
	}

	@Before
	public void setup_devices() {
		Mockito.when(devices.getConnectedDevice()).thenReturn(device);
		Mockito.when(device.stream()).thenReturn(stream);
	}

	@Test
	public void does_not_send_request_when_disconnected() {
		Mockito.when(device.getState()).thenReturn(State.Disconnected);

		testee.calc();
		testee.calc();
		testee.calc();

		verify(stream, never()).newData(any(List.class));
	}

	@Test
	public void does_not_send_request_when_connecting() {
		Mockito.when(device.getState()).thenReturn(State.Connecting);

		testee.calc();
		testee.calc();
		testee.calc();

		verify(stream, never()).newData(any(List.class));
	}

	@Test
	public void does_send_request_when_connected() {
		Mockito.when(device.getState()).thenReturn(State.Connected);

		testee.calc();

		verify(stream).newData(DefaultMessage);
	}

	@Test
	public void does_send_minimal_request_when_connected_and_timed_out() {
		Mockito.when(time.getTimeMs()).thenReturn(0l);
		Mockito.when(device.getState()).thenReturn(State.Connected);
		testee.calc();
		Mockito.when(time.getTimeMs()).thenReturn(10 * 1000l);

		testee.calc();

		verify(stream, times(1)).newData(MinimalMessage);
	}

	@Test
	public void does_not_send_request_when_disconnected_and_timed_out() {
		Mockito.when(time.getTimeMs()).thenReturn(0l);
		Mockito.when(device.getState()).thenReturn(State.Connected);
		testee.calc();
		Mockito.when(device.getState()).thenReturn(State.Disconnected);
		testee.calc();
		Mockito.when(time.getTimeMs()).thenReturn(10 * 1000l);

		testee.calc();

		verify(stream, never()).newData(MinimalMessage);
	}

}
