package com.netlab.frontend.objects.patterns.entityStrategy;

import com.netlab.frontend.objects.GameObject;

public class FixedMovement implements EntityMovementPattern {
    @Override
    public void move(GameObject entity, float delta) {
        // Fixed position: no displacement applied
    }
}
