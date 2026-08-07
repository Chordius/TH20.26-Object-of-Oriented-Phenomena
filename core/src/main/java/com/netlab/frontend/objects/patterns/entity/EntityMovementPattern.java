package com.netlab.frontend.objects.patterns.entity;

import com.netlab.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
