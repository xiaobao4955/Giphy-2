package com.alexeyosadchy.giphy.utils;

import com.alexeyosadchy.giphy.R;

import java.util.Random;

public final class AdapterUtils {

    public static int getRandomColor() {
        switch (new Random().nextInt(11)) {
            case 0:
                return R.color.place_holder_1;
            case 1:
                return R.color.place_holder_2;
            case 2:
                return R.color.place_holder_3;
            case 3:
                return R.color.place_holder_4;
            case 4:
                return R.color.place_holder_5;
            case 5:
                return R.color.place_holder_6;
            case 6:
                return R.color.place_holder_7;
            case 7:
                return R.color.place_holder_8;
            case 8:
                return R.color.place_holder_9;
            case 9:
                return R.color.place_holder_10;
            case 10:
                return R.color.place_holder_11;
            default:
                return R.color.place_holder_1;
        }
    }
}
