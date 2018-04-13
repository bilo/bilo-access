/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import ch.bitzgi.android.bluetooth.spp.event.sender.Abort;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Connected;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Connecting;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Disconnected;
import ch.bitzgi.android.bluetooth.spp.event.worker.Disconnect;
import ch.bitzgi.android.bluetooth.spp.event.worker.Event;
import ch.bitzgi.android.bluetooth.spp.event.worker.ReadError;
import ch.bitzgi.android.bluetooth.spp.event.worker.Visitor;
import ch.bitzgi.android.bluetooth.spp.queue.MessageHandler;
import ch.bitzgi.android.bluetooth.spp.queue.MessageSender;
import ch.bitzgi.android.bluetooth.spp.queue.thread.ThreadQueue;

class Worker extends Thread implements MessageHandler<Event> {
    private final MessageSender<ch.bitzgi.android.bluetooth.spp.event.supervisor.Event> toSupervisor;
    private final BluetoothDevice device;
    private final ThreadQueue<Event> queue = new ThreadQueue<>(this, this);
    private BluetoothSocket socket;
    private Receiver receiver;
    private Sender sender;
    private final Visitor dispatcher = new Visitor() {
        @Override
        public void visit(Disconnect event) {
            cancel();
        }

        @Override
        public void visit(ReadError event) {
            cancel();
        }
    };

    public Worker(MessageSender<ch.bitzgi.android.bluetooth.spp.event.supervisor.Event> toSupervisor, BluetoothDevice device) {
        this.toSupervisor = toSupervisor;
        this.device = device;
    }

    public void run() {
        toSupervisor.send(new Connecting("connection started"));

        socket = getBluetoothSocket(device);
        InputStream inStream = getInputStream(socket);

        setName("ConnectThread");

        if (!connect()) {
            toSupervisor.send(new Connecting("connection failed"));
            toSupervisor.send(Disconnected.Instance);
            return;
        }

        receiver = new Receiver(inStream, toSupervisor, queue.getSender());
        sender = new Sender(getOutputStream(socket), toSupervisor);

        receiver.start();
        sender.start();

        ///////////////////////////////////////

        toSupervisor.send(new Connecting("connected"));
        toSupervisor.send(Connected.Instance);

        while (socket.isConnected()) {
            queue.getReceiver().handle();
        }

        ///////////////////////////////////////

        sender.getQueue().send(Abort.Instance);

        try {
            sender.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        toSupervisor.send(Disconnected.Instance);
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    private InputStream getInputStream(BluetoothSocket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
        }

        throw new RuntimeException("could not get input stream");
    }

    private OutputStream getOutputStream(BluetoothSocket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
        }

        throw new RuntimeException("could not get toSupervisor stream");
    }

    private BluetoothSocket getBluetoothSocket(BluetoothDevice device) {
        UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            toSupervisor.send(new Connecting("try createInsecureRfcommSocketToServiceRecord"));
            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(sppUuid);
            toSupervisor.send(new Connecting("ok createInsecureRfcommSocketToServiceRecord"));
            return socket;
        } catch (IOException e) {
            toSupervisor.send(new Connecting("failed createInsecureRfcommSocketToServiceRecord"));
        }

        try {
            toSupervisor.send(new Connecting("try createRfcommSocketToServiceRecord"));
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(sppUuid);
            toSupervisor.send(new Connecting("ok createRfcommSocketToServiceRecord"));
            return socket;
        } catch (IOException e) {
            toSupervisor.send(new Connecting("failed createRfcommSocketToServiceRecord"));
        }

        try {
            toSupervisor.send(new Connecting("try createRfcommSocket"));
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
            toSupervisor.send(new Connecting("ok createRfcommSocket"));
            return socket;
        } catch (NoSuchMethodException e) {
            toSupervisor.send(new Connecting("failed createRfcommSocket"));
        } catch (IllegalAccessException e) {
            toSupervisor.send(new Connecting("failed createRfcommSocket"));
        } catch (InvocationTargetException e) {
            toSupervisor.send(new Connecting("failed createRfcommSocket"));
        }

        toSupervisor.send(new Connecting("no method to get bluetooth socket worked"));

        throw new RuntimeException("Could not get bluetooth socket");
    }

    private boolean connect() {
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            socket.connect();
            return true;
        } catch (IOException e) {
            // Close the socket
            try {
                socket.close();
            } catch (IOException e2) {
            }
            return false;
        }
    }

    public MessageSender<Event> getQueue() {
        return queue.getSender();
    }

    public MessageSender<ch.bitzgi.android.bluetooth.spp.event.sender.Event> getSendQueue() {
        return sender.getQueue();
    }

    @Override
    public void handle(Event event) {
        event.accept(dispatcher);
    }


}
