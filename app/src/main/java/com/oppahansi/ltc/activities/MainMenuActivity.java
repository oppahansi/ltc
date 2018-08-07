package com.oppahansi.ltc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.oppahansi.ltc.R;
import com.oppahansi.ltc.manager.StateManager;
import com.oppahansi.ltc.utils.Constants;
import com.oppahansi.ltc.utils.Utilities;

public class MainMenuActivity extends AppCompatActivity implements View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ImageButton vanilla = findViewById(R.id.vanillaButton);
        vanilla.setOnTouchListener(this);

        ImageButton tbc = findViewById(R.id.tbcButton);
        tbc.setOnTouchListener(this);

        ImageButton wotlk = findViewById(R.id.wotlkButton);
        wotlk.setOnTouchListener(this);

        Utilities.setMenuBackground(this, R.id.mainMenu);
        Utilities.initAndLoadAd(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vanillaButton: {
                StateManager.CURRENT_EXPANSION = Constants.Expansions.CLASSIC.ordinal();
                Intent intent = new Intent(MainMenuActivity.this, ClassMenuActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tbcButton: {
                StateManager.CURRENT_EXPANSION = Constants.Expansions.TBC.ordinal();
                Intent intent = new Intent(MainMenuActivity.this, ClassMenuActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.wotlkButton: {
                StateManager.CURRENT_EXPANSION = Constants.Expansions.WOTLK.ordinal();
                Intent intent = new Intent(MainMenuActivity.this, ClassMenuActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((ImageButton)view).setColorFilter(Color.argb(150, 0, 0, 0));
                return true;
            case MotionEvent.ACTION_UP:
                ((ImageButton)view).clearColorFilter();
                onClick(view);
                return true;
        }

        return false;
    }
}
