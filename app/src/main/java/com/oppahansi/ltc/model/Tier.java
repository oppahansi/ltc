package com.oppahansi.ltc.model;

import android.util.SparseArray;

import com.oppahansi.ltc.utils.Constants;

public class Tier {
    private SparseArray<Talent> talents;

    public Tier(SparseArray<Talent> talents) {
        this.talents = talents;
    }

    int tierSum() {
        int sum = 0;

        for (int i = 0; i < talents.size(); i++) {
            Talent talent = talents.get(i);

            if (talent != null)
                sum += talents.get(i).getPointsValue();
        }

        return sum;
    }

    void disableTalents(boolean disable) {
        for (int i = 0; i < talents.size(); i++) {
            Talent talent = talents.get(i);
            if (talent == null) continue;

            if (!disable) {
                Talent dependency = talent.getDependency();

                if (dependency != null) {
                    talent.disable(!(Constants.getCurrentTalentMaxPointsArray()[dependency.getId()] == dependency.getPointsValue()));
                } else {
                    talent.disable(disable);
                }
            }
            else
                talents.get(i).disable(disable);
        }
    }

    void disableZeroTalents(boolean disable) {
        for (int i = 0; i < talents.size(); i++) {
            Talent talent = talents.get(i);

            Talent dependency = talent.getDependency();

            if (dependency != null)
                talent.disable(!(Constants.getCurrentTalentMaxPointsArray()[dependency.getId()] == dependency.getPointsValue()));
            else if (talent.getPointsValue() == 0)
                talent.disable(disable);
        }
    }
}
