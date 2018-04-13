/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.ArrayList;
import java.util.List;

import world.bilo.stack.BlockId;
import world.bilo.stack.BlockType;
import world.bilo.stack.Rotation;

import static world.bilo.stack.stream.message.ByteConvertion.toByte;

class ModelSerializer {
    static public List<List<Byte>> serialize(Iterable<? extends BlockId> blocks) {
        List<List<Byte>> data = new ArrayList<>();

        for (BlockId block : blocks) {
            data.add(toLine(block));
        }

        return data;
    }

    static private ArrayList<Byte> toLine(BlockId block) {
        ArrayList<Byte> result = new ArrayList<>();
        result.add(toByte(block.position.x));
        result.add(toByte(block.position.y));
        result.add(toByte(block.position.z));
        result.add(toByte(getTypeAndRotation(block.type, block.rotation)));
        return result;
    }

    static private int getTypeAndRotation(BlockType type, Rotation rotation) {
        int typeNum = getTypeNum(type);
        int rotationNum = getRotationNum(rotation);
        int typeAndRotation = (typeNum << 2) | rotationNum;
        return typeAndRotation;
    }

    static private int getRotationNum(Rotation rotation) {
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

    static private int getTypeNum(BlockType type) {
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

}
