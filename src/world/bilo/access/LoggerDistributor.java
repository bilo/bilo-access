/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import world.bilo.stack.Logger;
import world.bilo.util.Observable;
import world.bilo.util.UniqueOrderedList;
import world.bilo.util.functional.Function;

public class LoggerDistributor implements Logger, Observable<Logger> {
	final private UniqueOrderedList<Logger> listeners = new UniqueOrderedList<>();

	@Override
	public void addListener(Logger listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(Logger listener) {
		listeners.remove(listener);
	}

	@Override
	public void error(final String message) {
		listeners.apply(new Function<Logger>() {
			@Override
			public void execute(Logger item) {
				item.error(message);
			}
		});
	}

	@Override
	public void debug(final String message) {
		listeners.apply(new Function<Logger>() {
			@Override
			public void execute(Logger item) {
				item.debug(message);
			}
		});
	}

}
