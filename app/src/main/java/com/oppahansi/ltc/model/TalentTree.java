package com.oppahansi.ltc.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oppahansi.ltc.R;
import com.oppahansi.ltc.manager.StateManager;
import com.oppahansi.ltc.utils.Constants;
import com.oppahansi.ltc.utils.RankDescriptionsProvider;
import com.oppahansi.ltc.utils.TalentIcons;
import com.oppahansi.ltc.utils.TalentNames;
import com.oppahansi.ltc.utils.Utilities;

public class TalentTree {
    private int spentPoints;
    private int requiredLevel;
    private int remainingPoints;

    private SparseIntArray talentPointsMap;
    private SparseIntArray talentTreesPointsMap;
    private SparseArray<TextView> talentTreeInfoTextViews;
    private SparseArray<Talent> talents;
    private SparseArray<Tier> tiers;

    public TalentTree(Activity activity) {
        setTalents(Utilities.buildTalents(activity));
        setTiers(Utilities.buildTiers(getTalents()));
        setTalentPointsMap(Utilities.buildTalentPoints(getTalents()));
        setTalentTreesPointsMap(Utilities.buildTalentTreesPoints());
        setTalentTreeInfoTextViews(Utilities.buildTalentTreeTextViews(activity));

        setLayout(activity);
        setBackground(activity);
        setTreeNames(activity);
        setInitialTalentPoints();
        syncViewToState(false);
    }

    public void setLayout(Activity activity) {
        for (int i = 0; i < talents.size(); i++) {
            if (Constants.getCurrentTreeLayout()[i] == 1) {
                Talent talent = talents.get(i);
                int talentIcon = activity.getResources().getIdentifier(TalentIcons.getCurrentTalentIcons()[i], "drawable", activity.getPackageName());
                talent.getButton().setImageDrawable(activity.getResources().getDrawable(talentIcon));
            }
        }

        for (int i = 1; i <= 3; i++) {
            int buttonId = activity.getResources().getIdentifier("tree" + i, "id", activity.getPackageName());
            int treeIconId = activity.getResources().getIdentifier(Constants.getCurrentTalentTreeIcons()[i - 1], "drawable", activity.getPackageName());
            ImageButton currentTree = activity.findViewById(buttonId);
            currentTree.setImageDrawable(activity.getResources().getDrawable(treeIconId));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showDialog(final Activity activity, final int talentId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ranks_dialog, null);
        dialogBuilder.setView(dialogView);

        final Talent talent = talents.get(talentId);

        final TextView talentName = dialogView.findViewById(R.id.rdTalentName);
        talentName.setText(TalentNames.getTalentName(talentId));

        final ImageButton add = dialogView.findViewById(R.id.addTalentPoint);
        final ImageButton remove = dialogView.findViewById(R.id.removeTalentPoint);

        final TextView currentRank = dialogView.findViewById(R.id.rdTalentRank);
        currentRank.setText(activity.getResources().getString(R.string.currentRank, talent.getPointsValue(), Constants.getCurrentTalentMaxPointsArray()[talentId]));

        final TextView currentRankDesc = dialogView.findViewById(R.id.rdTalentRankDesc);
        currentRankDesc.setText(RankDescriptionsProvider.getCurrentTalentRankDescriptions(talentId, talent.getPointsValue() == 0 ? 0 : talent.getPointsValue() - 1));

        final TextView nextRankDesc = dialogView.findViewById(R.id.rdTalentRankDescNext);
        nextRankDesc.setText("");

        checkNextRankDescription(talentId, talentPointsMap.get(talentId), nextRankDesc);
        checkRankDialogButtons(remove, add, talent);

        add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageButton) view).setColorFilter(Color.argb(150, 0, 0, 0));

                        return true;
                    case MotionEvent.ACTION_UP:
                        ((ImageButton) view).clearColorFilter();

                        int talentPoints = talentPointsMap.get(talentId) + 1;

                        talentPointsMap.put(talentId, talentPoints);
                        talents.get(talentId).setPointsValue(talentPoints);
                        StateManager.getCurrentTalentTreeState()[talentId] = talentPoints;
                        talentTreesPointsMap.put(StateManager.CURRENT_TALENT_TREE, talentTreesPointsMap.get(StateManager.CURRENT_TALENT_TREE) + 1);

                        currentRank.setText(activity.getResources().getString(R.string.currentRank, talentPoints, Constants.getCurrentTalentMaxPointsArray()[talentId]));
                        currentRankDesc.setText(RankDescriptionsProvider.getCurrentTalentRankDescriptions(talentId, talent.getPointsValue() - 1));

                        syncViewToState(false);
                        checkRankDialogButtons(remove, add, talent);
                        checkNextRankDescription(talentId, talentPoints, nextRankDesc);

                        return true;
                }
                return false;
            }
        });

        remove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageButton) view).setColorFilter(Color.argb(150, 0, 0, 0));

                        return true;
                    case MotionEvent.ACTION_UP:
                        ((ImageButton) view).clearColorFilter();

                        int talentPoints = talentPointsMap.get(talentId) - 1;

                        talentPointsMap.put(talentId, talentPoints);
                        talents.get(talentId).setPointsValue(talentPoints);
                        StateManager.getCurrentTalentTreeState()[talentId] = talentPoints;
                        talentTreesPointsMap.put(StateManager.CURRENT_TALENT_TREE, talentTreesPointsMap.get(StateManager.CURRENT_TALENT_TREE) - 1);

                        currentRank.setText(activity.getResources().getString(R.string.currentRank, talentPoints, Constants.getCurrentTalentMaxPointsArray()[talentId]));
                        currentRankDesc.setText(RankDescriptionsProvider.getCurrentTalentRankDescriptions(talentId, talentPoints > 0 ? talentPoints - 1 : 0));

                        syncViewToState(true);
                        checkRankDialogButtons(remove, add, talent);
                        checkNextRankDescription(talentId, talentPoints, nextRankDesc);

                        return true;
                }
                return false;
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void resetArrows() {
        for (int i = 0; i < talents.size(); i++) {
            Talent talent = talents.get(i);
            ImageView arrow = talent.getArrow();

            if (arrow != null) arrow.setVisibility(View.INVISIBLE);
        }
    }

    public void resetAll() {
        if (spentPoints == 0) return;

        StateManager.resetCurrentClass();

        for (int i = 0; i < talents.size(); i++) {
            talents.get(i).setPointsValue(0);
            talentPointsMap.put(i, 0);
        }

        for (int i = 0; i < talentTreesPointsMap.size(); i++) {
            talentTreesPointsMap.put(i, 0);
        }

        syncViewToState(true);
    }

    public void reset() {
        if (spentPoints == 0) return;

        StateManager.resetCurrentTalentTree();

        for (int i = 0; i < talents.size(); i++) {
            talents.get(i).setPointsValue(0);
            talentPointsMap.put(i, 0);
        }

        talentTreesPointsMap.put(StateManager.CURRENT_TALENT_TREE, 0);
        syncViewToState(true);
    }


    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public int getRemainingPoints() {
        return remainingPoints;
    }

    public void setRemainingPoints(int remainingPoints) {
        this.remainingPoints = remainingPoints;
    }

    public Talent getTalent(int id) {
        if (id >= 0 && id < talents.size()) {
            return talents.get(id);
        } else return null;
    }


    private void setBackground(Activity activity) {
        int treeIconId = activity.getResources().getIdentifier(Constants.getCurrentTalentTreeBackground(), "drawable", activity.getPackageName());
        ConstraintLayout layout = activity.findViewById(R.id.treeLayout);
        layout.setBackground(activity.getResources().getDrawable(treeIconId));
    }

    private void setTreeNames(Activity activity) {
        View treesNavigation = activity.findViewById(R.id.treesNavigation);

        TextView tree1Name = treesNavigation.findViewById(R.id.tree1Name);
        TextView tree2Name = treesNavigation.findViewById(R.id.tree2Name);
        TextView tree3Name = treesNavigation.findViewById(R.id.tree3Name);

        tree1Name.setText(Constants.getCurrentTreeNames()[0]);
        tree2Name.setText(Constants.getCurrentTreeNames()[1]);
        tree3Name.setText(Constants.getCurrentTreeNames()[2]);

        switch(StateManager.CURRENT_TALENT_TREE) {
            case 0:
                tree1Name.setTextColor(Color.GREEN);
                tree2Name.setTextColor(Color.WHITE);
                tree3Name.setTextColor(Color.WHITE);
                break;
            case 1:
                tree1Name.setTextColor(Color.WHITE);
                tree2Name.setTextColor(Color.GREEN);
                tree3Name.setTextColor(Color.WHITE);
                break;
            case 2:
                tree1Name.setTextColor(Color.WHITE);
                tree2Name.setTextColor(Color.WHITE);
                tree3Name.setTextColor(Color.GREEN);
                break;
        }
    }

    private void setInitialTalentPoints() {
        for (int i = 0; i < StateManager.getCurrentTalentTreeState().length; i++) {
            talents.get(i).setPointsValue(StateManager.getCurrentTalentTreeState()[i]);
            talentPointsMap.put(i, StateManager.getCurrentTalentTreeState()[i]);
        }
    }

    private void syncViewToState(boolean disableRest) {
        acquireSpentPoints();
        requiredLevel = 9 + spentPoints;
        remainingPoints = Constants.getCurrentMaxTalentPoints() - spentPoints;

        int max = ((talentTreesPointsMap.get(StateManager.CURRENT_TALENT_TREE) / 5) + 1);
        max = max > Constants.getCurrentMaxTalentRows() ? Constants.getCurrentMaxTalentRows() : max;

        for (int i = 1; i <= max; i++) {
            tiers.get(i).disableTalents(false);

            if (remainingPoints == 0)
                tiers.get(i).disableZeroTalents(true);
            else
                tiers.get(i).disableZeroTalents(false);
        }

        if (disableRest) {
            for (int i = max + 1; i <= tiers.size(); i++) {
                tiers.get(i).disableTalents(true);
            }
        }

        updateTalentTreeInfoTextViews();
    }

    private void updateTalentTreeInfoTextViews() {
        for (int i = 0; i < talentTreeInfoTextViews.size(); i++) {
            switch(i) {
                case 0:
                case 1:
                case 2:
                    talentTreeInfoTextViews.get(i).setText(String.valueOf(talentTreesPointsMap.get(i)));
                    break;
                case 3:
                    talentTreeInfoTextViews.get(i).setText(String.valueOf(requiredLevel));
                    break;
                case 4:
                    talentTreeInfoTextViews.get(i).setText(String.valueOf(remainingPoints));
                    break;
            }
        }
    }

    private void acquireSpentPoints() {
        int sum = 0;

        for (int i = 0; i < talentTreesPointsMap.size(); i++) {
            sum += talentTreesPointsMap.get(i);
        }

        spentPoints = sum;
    }

    private void checkNextRankDescription(int talentId, int talentPoints, TextView nextRankDesc) {
        if (talentPoints > 0 && talentPoints < Constants.getCurrentTalentMaxPointsArray()[talentId]) {
            nextRankDesc.setVisibility(View.VISIBLE);
            nextRankDesc.setText(RankDescriptionsProvider.getCurrentTalentRankDescriptions(talentId, talentPoints));
        } else {
            nextRankDesc.setVisibility(View.INVISIBLE);
        }
    }

    private void checkRankDialogButtons(ImageButton remove, ImageButton add, Talent talent) {
        if (!removePointPossible(talent)) {
            remove.setEnabled(false);

            final ColorMatrix grayscaleMatrix = new ColorMatrix();
            grayscaleMatrix.setSaturation(0);

            final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(grayscaleMatrix);
            remove.setColorFilter(filter);
        } else {
            remove.setEnabled(true);
            remove.setColorFilter(null);
        }

        if (talents.get(talent.getId()).getPointsValue() == Constants.getCurrentTalentMaxPointsArray()[talent.getId()] || spentPoints == Constants.getCurrentMaxTalentPoints() || talent.isDisabled()) {
            add.setEnabled(false);
            final ColorMatrix grayscaleMatrix = new ColorMatrix();
            grayscaleMatrix.setSaturation(0);

            final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(grayscaleMatrix);
            add.setColorFilter(filter);
        } else {
            add.setEnabled(true);
            add.setColorFilter(null);
        }
    }

    private boolean removePointPossible(Talent talent) {
        if (talentPointsMap.get(talent.getId()) == 0) return false;
        else if (pointsInDependency(talent.getId())) return false;
        else {
            int pointsInTree = talentTreesPointsMap.get(StateManager.CURRENT_TALENT_TREE);
            int activeRows = (pointsInTree / 5 + 1) > Constants.getCurrentMaxTalentRows() ? Constants.getCurrentMaxTalentRows() : (pointsInTree / 5 + 1);
            int activeRowSum = tiers.get(activeRows).tierSum();
            int pointsUpToTier = 0;

            for (int i = 1; i <= talent.getTier(); i++) {
                pointsUpToTier += tiers.get(i).tierSum();
            }

            if (talent.isDisabled() && talent.getTier() > activeRows) return false;

            if (tiers.get(talent.getTier()).tierSum() == talent.getTier() * 5 && activeRowSum > 0)
                return false;
            if (activeRowSum > 0 && pointsInTree - 1 == (activeRows - 1) * 5 && talent.getTier() != activeRows)
                return false;
            if (pointsUpToTier == talent.getTier() * 5 && tiers.get(talent.getTier() < Constants.getCurrentMaxTalentRows() ? talent.getTier() + 1 : Constants.getCurrentMaxTalentRows()).tierSum() > 0)
                return false;

            return true;
        }
    }

    private boolean pointsInDependency(int talentId) {
        for (int i = 0; i < Constants.getCurrentDependencyArray().length; i++) {
            int currentDependency = Constants.getCurrentDependencyArray()[i];
            if (currentDependency != -1 && talentId == currentDependency && talentPointsMap.get(i) != 0) {
                return true;
            }
        }

        return false;
    }


    private SparseArray<Talent> getTalents() {
        return talents;
    }

    private void setTalents(SparseArray<Talent> talents) {
        this.talents = talents;
    }

    private void setTiers(SparseArray<Tier> tiers) {
        this.tiers = tiers;
    }

    private void setTalentTreesPointsMap(SparseIntArray talentTreesPointsMap) {
        this.talentTreesPointsMap = talentTreesPointsMap;
    }

    private void setTalentPointsMap(SparseIntArray talentPointsMap) {
        this.talentPointsMap = talentPointsMap;
    }

    private void setTalentTreeInfoTextViews(SparseArray<TextView> talentTreeInfoTextViews) {
        this.talentTreeInfoTextViews = talentTreeInfoTextViews;
    }
}
