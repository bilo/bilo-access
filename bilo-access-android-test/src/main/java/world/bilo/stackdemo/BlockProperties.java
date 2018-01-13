/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bloctesian.Block;
import com.bloctesian.RgbLed;
import world.bilo.stackdemo.R;

public class BlockProperties extends Fragment {
    private Block block = null;
    private BlockSmall overview = null;
    private ArrayAdapter<RgbLed> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.block_properties, container, false);
        overview = (BlockSmall) view.findViewById(R.id.overview);

        ListView ledView = (ListView) view.findViewById(R.id.leds);
        adapter = new LedListViewAdapter(view.getContext(), android.R.layout.simple_list_item_1);
        ledView.setAdapter(adapter);

        return view;
    }

    public void setBlock(Block block) {
        this.block = block;
        update();
    }

    private void update() {
        adapter.clear();

        if (overview != null) {
            overview.set(block);
            if (block != null) {
                adapter.addAll(block.getLeds());
            }
        }

        adapter.notifyDataSetChanged();
    }

}
