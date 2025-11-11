package com.thecolonel63.ccmod.conditional;

import java.util.List;

public class MixinCanceller implements com.bawnorton.mixinsquared.api.MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        return mixinClassName.equals("de.maxhenkel.status.mixin.PlayerMixin");
    }
}
