/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.List;

import world.bilo.stack.Block;
import world.bilo.stack.Logger;
import world.bilo.stack.stream.Stream;
import world.bilo.stack.stream.StreamBlocks;
import world.bilo.stack.support.PollTimer;
import world.bilo.stack.support.Time;
import world.bilo.util.Observable;
import world.bilo.util.SetChangeDetector;
import world.bilo.util.UniqueOrderedList;
import world.bilo.util.ValueSet;

public class Bilo {
	private PollStream stream = new NullStream();
	private final UniqueOrderedList<ConnectionChangeObserver> connectionChangeObserver = new UniqueOrderedList<>();
	private final SetChangeDetector<Block> model = new SetChangeDetector<Block>(new ValueSet<Block>());
	private final PollTimer timer;
	private final LoggerDistributor stackDistributor = new LoggerDistributor();
	private final StreamBlocks api;
	private final Stream output = new Stream() {
		@Override
		public void newData(List<Byte> data) {
			stream.newData(data);
		}
	};
	private final Devices devices;
	private world.bilo.access.State last = world.bilo.access.State.Disconnected;

	private enum State { // TODO remove this state by simplifying code
		Disconnected, ReadyToRun, Running
	}

	private State state = State.Disconnected;

	public Bilo(Devices devices, Time time) {
		timer = new PollTimer(time);
		api = new StreamBlocks(output, timer, stackDistributor);
		this.devices = devices;
	}

	public void calc() {
		updateConnection();

		switch (state) {
		case Disconnected:
			break;

		case ReadyToRun:
			for (ConnectionChangeObserver observer : connectionChangeObserver) {
				observer.connected();
			}

			api.start();
			state = State.Running;
			break;

		case Running:
			List<Byte> data = stream.read();
			api.newData(data);

			model.changed(new ValueSet<>(api.getBlocks().items()));

			timer.poll();

			break;
		}
	}

	private void updateConnection() {
		Device device = devices.getConnectedDevice();
		world.bilo.access.State state = device.getState();

		if (last == state) {
			return;
		}
		last = state;

		switch (state) {
		case Connecting: {
			break;
		}
		case Connected: {
			connected(device);
			break;
		}
		case Disconnecting: {
			break;
		}
		case Disconnected: {
			disconnected();
			break;
		}
		}

	}

	public SetChangeDetector<Block> getModel() {
		return model;
	}

	private void connected(Device device) {
		stream = device.stream();
		state = State.ReadyToRun;
	}

	private void disconnected() {
		if (state == State.Running) {
			api.stop();
			model.changed(new ValueSet<Block>());
			state = State.ReadyToRun;
		}

		if (state == State.ReadyToRun) {
			stream = new NullStream();
			state = State.Disconnected;

			for (ConnectionChangeObserver observer : connectionChangeObserver) {
				observer.disconnected();
			}
		}
	}

	public Observable<Logger> getLoggerObservable() {
		return stackDistributor;
	}

	public StreamBlocks getApi() {
		return api;
	}

	public Devices getDevices() {
		return devices;
	}

	public UniqueOrderedList<ConnectionChangeObserver> getConnectionChangeObserver() {
		return connectionChangeObserver;
	}

}
