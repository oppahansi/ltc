package com.oppahansi.ltc.model;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oppahansi.ltc.utils.Constants;

public class Talent {
    private ImageButton button;
    private TextView points;
    private int tier;
    private int pointsValue;
    private int id;

    private boolean disabled;
    private Talent dependency;
    private ImageView arrow;

    public Talent(int id, int tier, final ImageButton button, TextView points) {
        this.id = id;
        this.button = button;
        this.points = points;
        this.tier = tier;
        this.disabled = false;
    }

    public void disable(boolean disabled) {
        this.disabled = disabled;
        if (disabled) grayOut();
        else clearColorFilter();
    }

    public int getId() {
        return id;
    }

    public int getTier() {
        return tier;
    }

    public int getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
        this.points.setText(String.valueOf(this.pointsValue));

        if (pointsValue == Constants.getCurrentTalentMaxPointsArray()[id]) {
            button.setBackgroundColor(Color.YELLOW);
            points.setTextColor(Color.YELLOW);
        } else {
            if (!disabled) {
                button.setBackgroundColor(Color.GREEN);
                points.setTextColor(Color.GREEN);
            } else {
                button.setBackgroundColor(Color.TRANSPARENT);
                points.setTextColor(Color.GRAY);
            }
        }
    }

    public Talent getDependency() {
        return dependency;
    }

    public void setDependency(Talent dependency) {
        this.dependency = dependency;
    }

    public void setArrow(ImageView arrow) {
        this.arrow = arrow;

        if (arrow != null) arrow.setVisibility(View.VISIBLE);
    }

    public ImageView getArrow() {
        return arrow;
    }

    public ImageButton getButton() {
        return button;
    }

    public TextView getPoints() {
        return points;
    }

    public boolean isDisabled() {
        return disabled;
    }

    private void grayOut() {
        final ColorMatrix grayscaleMatrix = new ColorMatrix();
        grayscaleMatrix.setSaturation(0);

        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(grayscaleMatrix);
        button.setColorFilter(filter);
        button.setBackgroundColor(Color.GRAY);
        points.setTextColor(Color.GRAY);

        if (dependency != null && arrow != null) arrow.setColorFilter(filter);
    }

    private void clearColorFilter() {
        button.setColorFilter(null);
        points.setTextColor(Color.GREEN);

        setPointsValue(getPointsValue());

        if (dependency != null && arrow != null) arrow.setColorFilter(null);
    }
}
