/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bloctesian.Block;
import com.bloctesian.utility.CollectionObserver;
import com.bloctesian.utility.ObservableCollection;

import java.util.Collection;

public class BlockListViewAdapter extends ArrayAdapter<Block> implements CollectionObserver<Block> {
    private final Context context;
    private final ObservableCollection<Block> collection;

    public BlockListViewAdapter(Context context, Block base, ObservableCollection<Block> collection) {
        super(context, -1);
        this.context = context;
        this.collection = collection;
        collection.listener().add(this);

        add(base);
        addAll(collection.items());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return new BlockSmall(parent.getContext(), getItem(position));
    }

    @Override
    public void added(Collection<Block> added) {
        addAll(added);
        notifyDataSetChanged();
    }

    @Override
    public void removed(Collection<Block> removed) {
        for (Block block : removed) {
            remove(block);
        }
        notifyDataSetChanged();
    }

}