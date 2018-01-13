/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.bloctesian.Color;
import com.bloctesian.RgbLed;

public class LedView extends LinearLayout {
    private final String ChannelNames[] = {"red", "green", "blue"};

    public LedView(Context context, RgbLed led) {
        super(context);

        setOrientation(LinearLayout.HORIZONTAL);

        createElements(led);
    }

    private void createElements(RgbLed led) {
        addChannel(0, led);
        addChannel(1, led);
        addChannel(2, led);
    }

    private void addChannel(int channel, RgbLed led) {
        CheckBox box = new CheckBox(getContext());
        box.setChecked(isChannelSet(channel, led));
        box.setText(ChannelNames[channel]);
        box.setOnClickListener(new ClickListener(channel, led));
        this.addView(box);
    }

    private boolean isChannelSet(int channel, RgbLed led) {
        return ((led.getColor().bitfield() >> channel) & 1) == 1;
    }

    class ClickListener implements OnClickListener {
        private final int channel;
        private final RgbLed led;

        ClickListener(int channel, RgbLed led) {
            this.channel = channel;
            this.led = led;
        }

        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();

            int colorCode = led.getColor().bitfield();
            int mask = 1 << channel;
            if (checked) {
                colorCode |= mask;
            } else {
                colorCode &= ~mask;
            }
            colorCode &= 0x7;
            Color color = Color.values()[colorCode];

            led.setColor(color);
        }
    }

}