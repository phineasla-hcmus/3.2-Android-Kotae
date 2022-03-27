package com.ogif.kotae.utils;

import android.view.View;

import androidx.annotation.NonNull;

import com.ogif.kotae.R;

import org.jetbrains.annotations.Nullable;

public final class ViewExtension {
    public static void setVisibilityMarker(@NonNull View view, boolean isVisible) {
        view.setTag(R.string.tag_view_extension, isVisible);
    }

    public static boolean getVisibilityMarker(@NonNull View view) {
        Object object = view.getTag(R.string.tag_view_extension);
        if (!(object instanceof Boolean)) {
            object = null;
        }

        Boolean var3 = (Boolean) object;
        boolean var4;
        if (var3 != null) {
            var4 = var3;
        } else {
            var4 = view.getVisibility() == View.VISIBLE;
        }

        return var4;
    }

    public static void setAnimationMarker(@NonNull View view, @NonNull Object marker) {
        view.setTag(R.string.tag_animation_maker, marker);
    }

    @Nullable
    public static Object getAnimationMarker(@NonNull View view) {
        return view.getTag(R.string.tag_animation_maker);
    }

    public static void cancelAllAnimations(@NonNull View view) {
        view.clearAnimation();
        view.animate().cancel();
    }
}
