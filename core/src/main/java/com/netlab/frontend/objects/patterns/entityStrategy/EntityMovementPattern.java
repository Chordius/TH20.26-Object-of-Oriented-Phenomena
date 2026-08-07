package com.netlab.frontend.objects.patterns.entityStrategy;

import com.netlab.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
