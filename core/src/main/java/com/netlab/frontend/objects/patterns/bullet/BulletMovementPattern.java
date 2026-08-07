package com.netlab.frontend.objects.patterns.bullet;

import com.netlab.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {
    void move(Bullet bullet, float delta);
}
