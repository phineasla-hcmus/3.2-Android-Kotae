package com.ogif.kotae.utils;

import android.view.View;

import androidx.annotation.NonNull;

import com.ogif.kotae.R;

import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class ViewExtension {
    public static final void setVisibilityMarker(@NonNull View $this$setVisibilityMarker, boolean isVisible) {
        $this$setVisibilityMarker.setTag(R.string.tag_view_extension, isVisible);
    }

    public static final boolean getVisibilityMarker(@NonNull View $this$getVisibilityMarker) {
        Object var10000 = $this$getVisibilityMarker.getTag(R.string.tag_view_extension);
        if (!(var10000 instanceof Boolean)) {
            var10000 = null;
        }

        Boolean var3 = (Boolean) var10000;
        boolean var4;
        if (var3 != null) {
            var4 = var3;
        } else {
            boolean $i$f$isVisible = false;
            var4 = $this$getVisibilityMarker.getVisibility() == View.VISIBLE;
        }

        return var4;
    }

    public static final void setAnimationMarker(@NonNull View $this$setAnimationMarker, @NonNull Object marker) {
        $this$setAnimationMarker.setTag(R.string.tag_animation_maker, marker);
    }

    @Nullable
    public static final Object getAnimationMarker(@NonNull View $this$getAnimationMarker) {
        Intrinsics.checkNotNullParameter($this$getAnimationMarker, "$this$getAnimationMarker");
        return $this$getAnimationMarker.getTag(R.string.tag_animation_maker);
    }

    public static final void cancelAllAnimations(@NonNull View $this$cancelAllAnimations) {
        Intrinsics.checkNotNullParameter($this$cancelAllAnimations, "$this$cancelAllAnimations");
        $this$cancelAllAnimations.clearAnimation();
        $this$cancelAllAnimations.animate().cancel();
    }
}
