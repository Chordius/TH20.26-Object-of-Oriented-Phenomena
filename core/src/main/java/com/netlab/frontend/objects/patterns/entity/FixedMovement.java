package com.netlab.frontend.objects.patterns.entity;

import com.netlab.frontend.objects.GameObject;

public class FixedMovement implements EntityMovementPattern {
    @Override
    public void move(GameObject entity, float delta) {
        // Fixed position: no displacement applied
    }
}
