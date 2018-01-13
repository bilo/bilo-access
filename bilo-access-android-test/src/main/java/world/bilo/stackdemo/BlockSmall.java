/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bloctesian.Block;
import world.bilo.stackdemo.R;

public class BlockSmall extends LinearLayout {
    private Block block = null;
    private TextView typeText = null;
    private TextView positionText = null;
    private TextView rotationText = null;

    public BlockSmall(Context context) {
        super(context);

        inflate(context);
    }

    public BlockSmall(Context context, Block block) {
        super(context);
        this.block = block;

        inflate(context);
    }

    public BlockSmall(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context);
    }

    private void inflate(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.block_small, this);

        typeText = (TextView) findViewById(R.id.type);
        positionText = (TextView) findViewById(R.id.position);
        rotationText = (TextView) findViewById(R.id.rotation);

        update();
    }

    public void set(Block block) {
        this.block = block;
        update();
    }

    private void update() {
        if (block != null) {
            typeText.setText(block.getId().type.toString());
            positionText.setText(block.getId().position.toString());
            rotationText.setText(block.getId().rotation.toString());
        }
    }
}
