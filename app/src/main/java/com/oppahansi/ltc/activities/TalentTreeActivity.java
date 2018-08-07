package com.oppahansi.ltc.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.oppahansi.ltc.R;
import com.oppahansi.ltc.manager.StateManager;
import com.oppahansi.ltc.model.TalentTree;
import com.oppahansi.ltc.utils.Constants;
import com.oppahansi.ltc.utils.Utilities;

public class TalentTreeActivity extends AppCompatActivity implements View.OnTouchListener {

    private TalentTree talentTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (StateManager.CURRENT_EXPANSION) {
            case 0:
                switch(StateManager.CURRENT_CLASS) {
                    case 0: setContentView(R.layout.activity_druid); break;
                    case 1: setContentView(R.layout.activity_hunter); break;
                    case 2: setContentView(R.layout.activity_mage); break;
                    case 3: setContentView(R.layout.activity_paladin); break;
                    case 4: setContentView(R.layout.activity_priest); break;
                    case 5: setContentView(R.layout.activity_rogue); break;
                    case 6: setContentView(R.layout.activity_shaman); break;
                    case 7: setContentView(R.layout.activity_warlock); break;
                    case 8: setContentView(R.layout.activity_warrior); break;
                }
                break;
            case 1:
                switch(StateManager.CURRENT_CLASS) {
                    case 0: setContentView(R.layout.activity_druid_tbc); break;
                    case 1: setContentView(R.layout.activity_hunter_tbc); break;
                    case 2: setContentView(R.layout.activity_mage_tbc); break;
                    case 3: setContentView(R.layout.activity_paladin_tbc); break;
                    case 4: setContentView(R.layout.activity_priest_tbc); break;
                    case 5: setContentView(R.layout.activity_rogue_tbc); break;
                    case 6: setContentView(R.layout.activity_shaman_tbc); break;
                    case 7: setContentView(R.layout.activity_warlock_tbc); break;
                    case 8: setContentView(R.layout.activity_warrior_tbc); break;
                }
                break;
            case 2:
                switch(StateManager.CURRENT_CLASS) {
                    case 0: setContentView(R.layout.activity_druid_wotlk); break;
                    case 1: setContentView(R.layout.activity_hunter_wotlk); break;
                    case 2: setContentView(R.layout.activity_mage_wotlk); break;
                    case 3: setContentView(R.layout.activity_paladin_wotlk); break;
                    case 4: setContentView(R.layout.activity_priest_wotlk); break;
                    case 5: setContentView(R.layout.activity_rogue_wotlk); break;
                    case 6: setContentView(R.layout.activity_shaman_wotlk); break;
                    case 7: setContentView(R.layout.activity_warlock_wotlk); break;
                    case 8: setContentView(R.layout.activity_warrior_wotlk); break;
                    case 9: setContentView(R.layout.activity_dk_wotlk); break;
                }
                break;
        }

        init();
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

                if (Utilities.viewIdToTalentId(((View)view.getParent()).getId()) != -1 && talentTree.getTalent(Utilities.viewIdToTalentId(((View)view.getParent()).getId())).isDisabled())
                    talentTree.getTalent(Utilities.viewIdToTalentId(((View)view.getParent()).getId())).disable(true);

                processTouch(view);
                return true;
        }
        return false;
    }

    public void processTouch(View view) {
        switch (view.getId()) {
            case R.id.saveButton: {
                showSaveDialog();
                break;
            }
            case R.id.openOnlineButton: {
                Uri uri = Uri.parse(Utilities.getWebUrlForCurrentBuild());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            }
            case R.id.resetAllButton: {
                talentTree.resetAll();
                break;
            }
            case R.id.resetTreeButton: {
                talentTree.reset();
                break;
            }
            case R.id.tree1: {
                if (StateManager.CURRENT_TALENT_TREE == 0) break;
                else {
                    StateManager.CURRENT_TALENT_TREE = 0;
                    talentTree.resetArrows();
                    init();
                    break;
                }
            }
            case R.id.tree2: {
                if (StateManager.CURRENT_TALENT_TREE == 1) break;
                else {
                    StateManager.CURRENT_TALENT_TREE = 1;
                    talentTree.resetArrows();
                    init();
                    break;
                }
            }
            case R.id.tree3: {
                if (StateManager.CURRENT_TALENT_TREE == 2) break;
                else {
                    StateManager.CURRENT_TALENT_TREE = 2;
                    talentTree.resetArrows();
                    init();
                    break;
                }
            }
            default: {
                talentTree.showDialog(this, Utilities.viewIdToTalentId(((View)view.getParent()).getId()));
                break;
            }
        }
    }

    private void showSaveDialog() {
        final SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAMES[StateManager.CURRENT_EXPANSION], MODE_PRIVATE);
        final EditText input = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setTitle("Save Build")
            .setMessage("Enter a name for this build.")
            .setView(input)
            .setPositiveButton("Done", null)
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!preferences.contains(input.getText().toString())) {
                            StringBuilder classStateStringBuilder = new StringBuilder(StateManager.CURRENT_CLASS + "");

                            for (int[] talentTree : StateManager.getCurrentTalentClassState()) {
                                for (Integer talent : talentTree) {
                                    classStateStringBuilder.append(talent);
                                }
                            }

                            preferences.edit().putString(input.getText().toString().toLowerCase(), classStateStringBuilder.toString()).apply();
                            Toast.makeText(getApplicationContext(), "Build has been saved.", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Build with this name already exists. Please enter a different build name.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void init() {
        talentTree = new TalentTree(this);

        initListeners();
        Utilities.initAndLoadAd(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        View treesNavi = findViewById(R.id.treesNavigation);

        ImageButton tree1Button = treesNavi.findViewById(R.id.tree1);
        ImageButton tree2Button = treesNavi.findViewById(R.id.tree2);
        ImageButton tree3Button = treesNavi.findViewById(R.id.tree3);
        ImageButton resetTreesButton = treesNavi.findViewById(R.id.resetAllButton);
        tree1Button.setOnTouchListener(this);
        tree2Button.setOnTouchListener(this);
        tree3Button.setOnTouchListener(this);
        resetTreesButton.setOnTouchListener(this);

        View treeOptions = findViewById(R.id.treeOptions);

        ImageButton resetTreeButton = treeOptions.findViewById(R.id.resetTreeButton);
        ImageButton webButton = treeOptions.findViewById(R.id.openOnlineButton);
        ImageButton saveButton = treeOptions.findViewById(R.id.saveButton);
        resetTreeButton.setOnTouchListener(this);
        webButton.setOnTouchListener(this);
        saveButton.setOnTouchListener(this);
    }
}
