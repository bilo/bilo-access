/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bloctesian.Block;
import world.bilo.stackdemo.R;

import world.bilo.stackdemo.api.Blocks;
import world.bilo.stackdemo.bluetooth.Devices;

//TODO handle device rotation

public class DeviceViewActivity extends AppCompatActivity implements DisconnectHandler {
    final private Blocks blocks = new Blocks(this);
    private BlockProperties blockProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.device_view_activity);

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");

        BluetoothDevice device = Devices.find(address);
        if (device == null) {
            disconnected();
        }

        ListView blocksView = (ListView) findViewById(R.id.blocks);
        BlockListViewAdapter adapter = new BlockListViewAdapter(this, blocks.getBase(), blocks.getBlocks());
        blocksView.setAdapter(adapter);

        blockProperties = (BlockProperties) getSupportFragmentManager().findFragmentById(R.id.block_fragment);

        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Block block = (Block) parent.getItemAtPosition(position);
                blockProperties.setBlock(block);
            }
        };
        blocksView.setOnItemClickListener(mMessageClickedHandler);

        blocks.connect(device);
    }

    @Override
    protected void onDestroy() {
        blocks.disconnect();
        super.onDestroy();
    }

    @Override
    public void disconnected() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
