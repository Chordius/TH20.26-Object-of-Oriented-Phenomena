package com.netlab.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;

public class Boss extends Enemy {
    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
    }
}
