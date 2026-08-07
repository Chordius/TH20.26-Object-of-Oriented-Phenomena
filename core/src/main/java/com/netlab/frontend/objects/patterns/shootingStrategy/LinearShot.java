package com.netlab.frontend.objects.patterns.shootingStrategy;

import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.systems.BulletManager;

public class LinearShot implements ShootingPattern {
    private float bulletSpeed;
    private int damage;

    public LinearShot(float bulletSpeed, int damage) {
        this.bulletSpeed = bulletSpeed;
        this.damage = damage;
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        // Angle: 90 degrees (Upward) for Player, 270 degrees (Downward) for Enemy
        float angle = isPlayer ? 90f : 270f;
        float rad = (float) Math.toRadians(angle);
        float vx = (float) (Math.cos(rad) * bulletSpeed);
        float vy = (float) (Math.sin(rad) * bulletSpeed);

        if (isPlayer) {
            bulletManager.spawnPlayerBullet(originX, originY, vx, vy, damage);
        } else {
            bulletManager.spawnEnemyBullet(originX, originY, vx, vy, damage);
        }
    }

    public float getBulletSpeed() { return bulletSpeed; }
    public void setBulletSpeed(float bulletSpeed) { this.bulletSpeed = bulletSpeed; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
}
