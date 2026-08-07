package com.netlab.frontend.objects.patterns.entity;

import com.netlab.frontend.objects.GameObject;

public class ZigzagEntityMovement implements EntityMovementPattern {
    private float speedY;
    private float frequency;
    private float amplitude;
    private float time = 0f;

    public ZigzagEntityMovement(float speedY, float frequency, float amplitude) {
        this.speedY = speedY;
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    @Override
    public void move(GameObject entity, float delta) {
        if (entity != null) {
            time += delta;
            float dx = (float) Math.cos(time * frequency) * amplitude * delta;
            float dy = speedY * delta;
            entity.setX(entity.getX() + dx);
            entity.setY(entity.getY() + dy);
        }
    }
}
