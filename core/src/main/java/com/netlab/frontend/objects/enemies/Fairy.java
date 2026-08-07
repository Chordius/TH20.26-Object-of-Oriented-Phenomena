package com.netlab.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.netlab.frontend.objects.patterns.shooting.LinearShot;
import com.netlab.frontend.objects.patterns.entity.ZigzagEntityMovement;

public class Fairy extends Enemy {
    public Fairy(String name, int hp) {
        super(150, 380, 24, 24, Color.PINK, name, hp, 500L);
        setShootingPattern(new LinearShot(150f, 10));
        setMovementPattern(new ZigzagEntityMovement(-40f, 4f, 60f));
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24, 24, Color.PINK, name, hp, 500L);
        setShootingPattern(new LinearShot(150f, 10));
        setMovementPattern(new ZigzagEntityMovement(-40f, 4f, 60f));
    }
}
