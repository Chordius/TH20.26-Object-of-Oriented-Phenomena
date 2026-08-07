package com.netlab.frontend.objects.patterns.entityStrategy;

import com.netlab.frontend.objects.GameObject;

public class LinearEntityMovement implements EntityMovementPattern {
    private float vx;
    private float vy;

    public LinearEntityMovement(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public void move(GameObject entity, float delta) {
        if (entity != null) {
            entity.setX(entity.getX() + vx * delta);
            entity.setY(entity.getY() + vy * delta);
        }
    }
}
