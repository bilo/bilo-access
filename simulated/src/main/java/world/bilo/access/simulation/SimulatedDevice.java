/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import static world.bilo.stack.stream.message.ByteConvertion.toByte;

import java.util.ArrayList;
import java.util.List;

import world.bilo.access.Device;
import world.bilo.access.PollStream;
import world.bilo.access.State;
import world.bilo.stack.BlockId;
import world.bilo.stack.BlockType;
import world.bilo.stack.Rotation;
import world.bilo.stack.support.Time;

//TODO add tests and move to blockstack
public class SimulatedDevice implements Device, PollStream {
  private final Time time;
  private final String name;
  private final ArrayList<ArrayList<Byte>> blocks = new ArrayList<>();
  private long lastSent = 0;
  private boolean pendingRequest = false;

  public SimulatedDevice(SimpleModel model, Time time) {
    this.time = time;

    name = "Simulated: " + model.name();
    for (BlockId block : model.blocks()) {
      blocks.add(toLine(block));
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public State getState() {
    return State.Connected;
  }

  @Override
  public List<Byte> read() {
    if (!pendingRequest) {
      return new ArrayList<>();
    }

    long now = time.getTimeMs() / 1000;

    if (lastSent == now) {
      return new ArrayList<>();
    }
    lastSent = now;
    pendingRequest = false;

    int step = (int) (now % (2 * blocks.size()));

    ArrayList<Byte> oneBlock = new ArrayList<>();
    oneBlock.add(toByte(0x80));

    if (step <= blocks.size()) {
      for (int i = 0; i < step; i++) {
        oneBlock.addAll(blocks.get(i));
      }
    } else {
      int start = step - blocks.size();
      for (int i = start; i < blocks.size(); i++) {
        oneBlock.addAll(blocks.get(i));
      }
    }

    oneBlock.add(toByte(0x81));

    return oneBlock;
  }

  @Override
  public void newData(List<Byte> data) {
    pendingRequest = true;
  }

  private ArrayList<Byte> toLine(BlockId block) {
    ArrayList<Byte> result = new ArrayList<>();
    result.add(toByte(block.position.x));
    result.add(toByte(block.position.y));
    result.add(toByte(block.position.z));
    result.add(toByte(getTypeAndRotation(block.type, block.rotation)));
    return result;
  }

  private int getTypeAndRotation(BlockType type, Rotation rotation) {
    int typeNum = getTypeNum(type);
    int rotationNum = getRotationNum(rotation);
    int typeAndRotation = (typeNum << 2) | rotationNum;
    return typeAndRotation;
  }

  private int getRotationNum(Rotation rotation) {
    int rotationNum;

    switch (rotation) {
      case Deg0:
        rotationNum = 0;
        break;
      case Deg270:
        rotationNum = 1;
        break;
      case Deg180:
        rotationNum = 2;
        break;
      case Deg90:
        rotationNum = 3;
        break;
      default:
        throw new RuntimeException("unknown rotation: " + rotation);
    }
    return rotationNum;
  }

  private int getTypeNum(BlockType type) {
    int typeNum;

    switch (type) {
      case Block4x2:
        typeNum = 0;
        break;
      case Block2x2:
        typeNum = 1;
        break;
      default:
        throw new RuntimeException("unknown type: " + type);
    }
    return typeNum;
  }

  @Override
  public PollStream stream() {
    return this;
  }

}
