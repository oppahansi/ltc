package com.oppahansi.ltc.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.oppahansi.ltc.R;
import com.oppahansi.ltc.manager.StateManager;
import com.oppahansi.ltc.utils.Constants;
import com.oppahansi.ltc.utils.Utilities;

public class ClassMenuActivity extends AppCompatActivity implements View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (StateManager.CURRENT_EXPANSION) {
            case 0:
                setContentView(R.layout.activity_vanilla_menu);
                Utilities.setMenuBackground(this, R.id.vanillaMenu);
                break;
            case 1:
                setContentView(R.layout.activity_tbc_menu);
                Utilities.setMenuBackground(this, R.id.tbcMenu);
                break;
            case 2:
                setContentView(R.layout.activity_wotlk_menu);
                Utilities.setMenuBackground(this, R.id.wotlkMenu);
                break;
        }

        Utilities.initAndLoadAd(this);

        ImageButton druid = findViewById(R.id.classDruid);
        druid.setOnTouchListener(this);

        ImageButton hunter = findViewById(R.id.classHunter);
        hunter.setOnTouchListener(this);

        ImageButton mage = findViewById(R.id.classMage);
        mage.setOnTouchListener(this);

        ImageButton paladin = findViewById(R.id.classPaladin);
        paladin.setOnTouchListener(this);

        ImageButton priest = findViewById(R.id.classPriest);
        priest.setOnTouchListener(this);

        ImageButton rogue = findViewById(R.id.classRogue);
        rogue.setOnTouchListener(this);

        ImageButton shaman = findViewById(R.id.classShaman);
        shaman.setOnTouchListener(this);

        ImageButton warlock = findViewById(R.id.classWarlock);
        warlock.setOnTouchListener(this);

        ImageButton warrior = findViewById(R.id.classWarrior);
        warrior.setOnTouchListener(this);

        if (StateManager.CURRENT_EXPANSION == 2) {
            ImageButton dk = findViewById(R.id.classDk);
            dk.setOnTouchListener(this);
        }

        ImageButton load = findViewById(R.id.loadButton);
        load.setOnTouchListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.classDruid: {
                StateManager.CURRENT_CLASS = Constants.Classes.DRUID.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.classHunter: {
                StateManager.CURRENT_CLASS = Constants.Classes.HUNTER.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classMage: {
                StateManager.CURRENT_CLASS = Constants.Classes.MAGE.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classPaladin: {
                StateManager.CURRENT_CLASS = Constants.Classes.PALADIN.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classPriest: {
                StateManager.CURRENT_CLASS = Constants.Classes.PRIEST.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classRogue: {
                StateManager.CURRENT_CLASS = Constants.Classes.ROGUE.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classShaman: {
                StateManager.CURRENT_CLASS = Constants.Classes.SHAMAN.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classWarlock: {
                StateManager.CURRENT_CLASS = Constants.Classes.WARLOCK.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classWarrior: {
                StateManager.CURRENT_CLASS = Constants.Classes.WARRIOR.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.classDk: {
                StateManager.CURRENT_CLASS = Constants.Classes.DK.ordinal();
                StateManager.CURRENT_TALENT_TREE = 0;

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.loadButton: {
                showLoadDialog();
                break;
            }
        }
    }

    private void showLoadDialog() {
        final SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAMES[StateManager.CURRENT_EXPANSION], MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(ClassMenuActivity.this);
        builder.setTitle("Select build to load");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ClassMenuActivity.this, android.R.layout.select_dialog_singlechoice);

        for (String build : preferences.getAll().keySet()) {
            arrayAdapter.add(build);
        }

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String build = arrayAdapter.getItem(which);

                if (build == null || build.length() == 0) {
                    dialog.dismiss();
                    return;
                }

                String buildString = preferences.getString(build, "0");
                int classBuild = Integer.parseInt(buildString.toCharArray()[0] + "");

                StateManager.CURRENT_CLASS = classBuild;
                StateManager.CURRENT_TALENT_TREE = 0;

                int counter = 1;

                for (int i = 0; i < StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][classBuild].length; i++) {
                    for (int j = 0; j < StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][classBuild][i].length; j++) {
                        StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][classBuild][i][j] = Integer.parseInt(buildString.charAt(counter) + "");
                        counter++;
                    }
                }

                Intent intent = new Intent(ClassMenuActivity.this, TalentTreeActivity.class);

                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        builder.show();
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
