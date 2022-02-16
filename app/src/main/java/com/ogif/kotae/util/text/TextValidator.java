package com.ogif.kotae.util.text;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Shortcut for {@link TextWatcher#afterTextChanged(Editable)}
 */
public abstract class TextValidator implements TextWatcher {

    @Override
    public final void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public final void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        validate(editable);
    }

    public abstract void validate(Editable editable);
}
