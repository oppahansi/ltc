package com.oppahansi.ltc.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.oppahansi.ltc.R;
import com.oppahansi.ltc.manager.StateManager;
import com.oppahansi.ltc.model.Talent;
import com.oppahansi.ltc.model.Tier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utilities {
    public static void setMenuBackground(Activity activity, int id) {
        Random rng = new Random();
        ConstraintLayout mainLayout = activity.findViewById(id);
        List<Integer> backgrounds = new ArrayList<>(
            Arrays.asList(
                R.drawable.affliction, R.drawable.arcane, R.drawable.arms, R.drawable.assassination, R.drawable.balance, R.drawable.beast_mastery,
                R.drawable.combat, R.drawable.demonology, R.drawable.destruction, R.drawable.discipline, R.drawable.elemental, R.drawable.enhancement,
                R.drawable.feral_combat, R.drawable.fire, R.drawable.frost, R.drawable.fury, R.drawable.holy, R.drawable.marksmanship, R.drawable.protection,
                R.drawable.restoration, R.drawable.retribution, R.drawable.shadow, R.drawable.subtlety, R.drawable.survival, R.drawable.holypriest,
                R.drawable.restorationdruid, R.drawable.protectionwarrior));

        if (id == R.id.mainMenu || id == R.id.wotlkMenu)
            backgrounds.addAll(Arrays.asList(R.drawable.blood, R.drawable.frostdk, R.drawable.unholy));

        mainLayout.setBackground(activity.getResources().getDrawable(backgrounds.get(rng.nextInt(id == R.id.mainMenu || id == R.id.wotlkMenu ? 30 : 27))));
    }

    @SuppressLint("ClickableViewAccessibility")
    public static SparseArray<Talent> buildTalents(Activity activity) {
        int id = 0;
        SparseArray<Talent> talents = new SparseArray<>();

        for (int i = 1; i <= Constants.getCurrentMaxTalentRows(); i++) {
            for (int j = 1; j <= 4; j++) {
                int includeId = activity.getResources().getIdentifier("lvl" + i + "" + j, "id", activity.getPackageName());

                View includeView = activity.findViewById(includeId);

                ImageButton button = includeView.findViewById(R.id.button);
                TextView points = includeView.findViewById(R.id.points);

                button.setOnTouchListener((View.OnTouchListener) activity);

                Talent talent = new Talent(id, i, button, points);

                if (Constants.getCurrentTreeLayout()[id] == 0) includeView.setVisibility(View.INVISIBLE);
                else includeView.setVisibility(View.VISIBLE);

                talents.append(id, talent);
                id++;
            }
        }

        id = 0;

        for (int i = 1; i <= Constants.getCurrentMaxTalentRows(); i++) {
            for (int j = 1; j <= 4; j++) {
                int dependency = Constants.getCurrentDependencyArray()[id];
                Talent talent = talents.get(id);

                if (dependency != -1) {
                    talent.setDependency(talents.get(dependency));
                    int arrowId = activity.getResources().getIdentifier("arrow" + StateManager.CURRENT_EXPANSION + "" + StateManager.CURRENT_CLASS + "" + StateManager.CURRENT_TALENT_TREE + "" + id, "id", activity.getPackageName());
                    talent.setArrow((ImageView) activity.findViewById(arrowId));
                }

                if (i > 1) talent.disable(true);

                id++;
            }
        }

        return talents;
    }

    public static SparseArray<Tier> buildTiers(SparseArray<Talent> talentsArray) {
        SparseArray<Tier> tiers = new SparseArray<>();

        int id = 0;

        for (int i = 1; i <= Constants.getCurrentMaxTalentRows(); i++) {
            SparseArray<Talent> talents = new SparseArray<>();

            for (int j = 0; j < 4; j++) {
                talents.append(j, talentsArray.get(id));
                id++;
            }

            Tier tier = new Tier(talents);
            tiers.append(i, tier);
        }

        return tiers;
    }

    public static SparseIntArray buildTalentPoints(SparseArray<Talent> talents) {
        SparseIntArray talentPointsMap = new SparseIntArray();

        for (int i = 0; i < talents.size(); i++) {
            talentPointsMap.append(i, talents.get(i).getPointsValue());
        }

        return talentPointsMap;
    }

    public static SparseIntArray buildTalentTreesPoints() {
        SparseIntArray talentTreesPointsMap = new SparseIntArray();

        for (int i = 0; i < StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS].length; i++) {
            int sum = 0;

            for (int j = 0; j < StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS][i].length; j++) {
                sum += StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS][i][j];
            }

            talentTreesPointsMap.append(i, sum);
        }

        return talentTreesPointsMap;
    }

    public static SparseArray<TextView> buildTalentTreeTextViews(Activity activity) {
        SparseArray<TextView> textViews = new SparseArray<>();

        View treeInfoView = activity.findViewById(R.id.treeInfo);
        View treeNavigationView = activity.findViewById(R.id.treesNavigation);

        TextView tree1 = treeNavigationView.findViewById(R.id.tree1Points);
        TextView tree2 = treeNavigationView.findViewById(R.id.tree2Points);
        TextView tree3 = treeNavigationView.findViewById(R.id.tree3Points);
        TextView requiredLevel = treeInfoView.findViewById(R.id.requiredLvl);
        TextView remainingPoints = treeInfoView.findViewById(R.id.remainingPoints);

        textViews.append(0, tree1);
        textViews.append(1, tree2);
        textViews.append(2, tree3);
        textViews.append(3, requiredLevel);
        textViews.append(4, remainingPoints);

        return textViews;
    }

    public static void initAndLoadAd(Activity activity) {
        MobileAds.initialize(activity, activity.getString(R.string.adUnitId));
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = activity.findViewById(R.id.adView);

        adView.loadAd(adRequest);
    }

    public static int viewIdToTalentId(int viewId) {
        switch (viewId) {
            case R.id.lvl11:
                return 0;
            case R.id.lvl12:
                return 1;
            case R.id.lvl13:
                return 2;
            case R.id.lvl14:
                return 3;
            case R.id.lvl21:
                return 4;
            case R.id.lvl22:
                return 5;
            case R.id.lvl23:
                return 6;
            case R.id.lvl24:
                return 7;
            case R.id.lvl31:
                return 8;
            case R.id.lvl32:
                return 9;
            case R.id.lvl33:
                return 10;
            case R.id.lvl34:
                return 11;
            case R.id.lvl41:
                return 12;
            case R.id.lvl42:
                return 13;
            case R.id.lvl43:
                return 14;
            case R.id.lvl44:
                return 15;
            case R.id.lvl51:
                return 16;
            case R.id.lvl52:
                return 17;
            case R.id.lvl53:
                return 18;
            case R.id.lvl54:
                return 19;
            case R.id.lvl61:
                return 20;
            case R.id.lvl62:
                return 21;
            case R.id.lvl63:
                return 22;
            case R.id.lvl64:
                return 23;
            case R.id.lvl71:
                return 24;
            case R.id.lvl72:
                return 25;
            case R.id.lvl73:
                return 26;
            case R.id.lvl74:
                return 27;
            case R.id.lvl81:
                return 28;
            case R.id.lvl82:
                return 29;
            case R.id.lvl83:
                return 30;
            case R.id.lvl84:
                return 31;
            case R.id.lvl91:
                return 32;
            case R.id.lvl92:
                return 33;
            case R.id.lvl93:
                return 34;
            case R.id.lvl94:
                return 35;
            case R.id.lvl101:
                return 36;
            case R.id.lvl102:
                return 37;
            case R.id.lvl103:
                return 38;
            case R.id.lvl104:
                return 39;
            case R.id.lvl111:
                return 40;
            case R.id.lvl112:
                return 41;
            case R.id.lvl113:
                return 42;
            case R.id.lvl114:
                return 43;
            default:
                return -1;
        }
    }

    public static String getWebUrlForCurrentBuild() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS].length; i++) {
            for (int j = 0; j < StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS][i].length; j++) {
                if (Constants.TALENT_TREE_LAYOUTS[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS][i][j] == 1)
                    builder.append(StateManager.TALENT_TREES_STATE[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS][i][j]);
            }
        }

        return Constants.BASE_CLASS_URLS[StateManager.CURRENT_EXPANSION][StateManager.CURRENT_CLASS] + builder.toString();
    }
}
