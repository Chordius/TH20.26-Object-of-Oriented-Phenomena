package com.netlab.frontend.objects.patterns;

import com.netlab.frontend.systems.BulletManager;

public class LinearShot implements ShootingPattern {
    private float speed;

    public LinearShot(float speed) {
        this.speed = speed;
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        float vy = isPlayer ? speed : -speed;
        if (isPlayer) {
            bulletManager.spawnPlayerBullet(originX, originY, 0, vy, 25);
        } else {
            bulletManager.spawnEnemyBullet(originX, originY, 0, vy, 15);
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
}
