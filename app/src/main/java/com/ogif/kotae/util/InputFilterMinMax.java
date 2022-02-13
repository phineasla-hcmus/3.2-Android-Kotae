package com.ogif.kotae.util;

import android.text.InputFilter;
import android.text.Spanned;


/**
 * https://stackoverflow.com/a/14212734/12405558
 */
public class InputFilterMinMax implements InputFilter {

    private final int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Removes string that is to be replaced from destination and adds the new string in.
        String newVal = dest.subSequence(0, dstart)
                // Note that below "toString()" is the only required:
                + source.subSequence(start, end).toString()
                + dest.subSequence(dend, dest.length());
        try {
            int input = Integer.parseInt(newVal);
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException ex) {
            return null;
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
