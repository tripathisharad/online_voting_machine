package com.votingmachine.votingmachine20;

import static java.lang.Math.min;

import android.view.View;

import androidx.annotation.NonNull;

import androidx.viewpager2.widget.ViewPager2;

public class ForegroundToBackgroundPageTransformer implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float pos) {
        final float height = page.getHeight();
        final float width = page.getWidth();
        final float scale = min(pos > 0 ? 1f : Math.abs(1f + pos), 1f);

        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setPivotX(width * 0.5f);
        page.setPivotY(height * 0.5f);
        page.setTranslationX(pos > 0 ? width * pos : -width * pos * 0.25f);
    }
}