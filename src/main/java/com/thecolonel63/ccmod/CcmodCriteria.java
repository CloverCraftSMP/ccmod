package com.thecolonel63.ccmod;

import com.thecolonel63.ccmod.criterion.PingCriterion;
import net.minecraft.advancement.criterion.Criteria;

public class CcmodCriteria {
    public static final PingCriterion PING_CRITERION = Criteria.register("ccmod:have_ping", new PingCriterion());

    public static void init() {

    }
}
