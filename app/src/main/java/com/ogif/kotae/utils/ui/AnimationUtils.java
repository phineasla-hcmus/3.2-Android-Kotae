package com.ogif.kotae.utils.ui;

import static com.ogif.kotae.utils.ui.ViewExtension.setAnimationMarker;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.paulrybitskyi.commons.ktx.ViewUtils;
import com.paulrybitskyi.persistentsearchview.utils.AnimationType;

public final class AnimationUtils {
    private static final long HEADER_ANIMATION_DURATION;
    private static final DecelerateInterpolator HEADER_ANIMATION_INTERPOLATOR;
    @NonNull
    public static final AnimationUtils INSTANCE;

    public final void showHeader(@NonNull View header) {
        if (!ViewExtension.getVisibilityMarker(header) && AnimationType.ENTER != ViewExtension.getAnimationMarker(header)) {
            ViewExtension.cancelAllAnimations(header);
            ViewUtils.makeVisible(header);
            ViewExtension.setVisibilityMarker(header, true);
            header.animate().translationY(0.0F).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    setAnimationMarker(header, AnimationType.ENTER);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    setAnimationMarker(header, AnimationType.NO_ANIMATION);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).setInterpolator(HEADER_ANIMATION_INTERPOLATOR)
                    .setDuration(HEADER_ANIMATION_DURATION)
                    .start();
        }
    }

    public final void hideHeader(@NonNull View header) {
        if (ViewExtension.getVisibilityMarker(header) && AnimationType.EXIT != ViewExtension.getAnimationMarker(header)) {
            ViewExtension.cancelAllAnimations(header);
            ViewExtension.setVisibilityMarker(header, false);
            header.animate().translationY(-((float) header.getMeasuredHeight()))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            setAnimationMarker(header, AnimationType.EXIT);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            setAnimationMarker(header, AnimationType.NO_ANIMATION);
                            header.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    }).setInterpolator((TimeInterpolator) HEADER_ANIMATION_INTERPOLATOR)
                    .setDuration(HEADER_ANIMATION_DURATION)
                    .start();
        }
    }

    private AnimationUtils() {
    }

    static {
        INSTANCE = new AnimationUtils();
        HEADER_ANIMATION_DURATION = 250L;
        HEADER_ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    }
}