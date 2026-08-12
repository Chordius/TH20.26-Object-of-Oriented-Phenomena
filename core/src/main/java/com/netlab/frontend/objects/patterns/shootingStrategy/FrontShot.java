package com.netlab.frontend.objects.patterns.shootingStrategy;

import com.netlab.frontend.objects.bullets.Bullet;
import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.netlab.frontend.systems.BulletManager;

public class FrontShot implements ShootingPattern {
    private float bulletSpeed;
    private int damage;
    private BulletMovementPattern bulletMovementPattern;

    public FrontShot(float bulletSpeed, int damage, BulletMovementPattern bulletMovementPattern) {
        this.bulletSpeed = bulletSpeed;
        this.damage = damage;
        this.bulletMovementPattern = bulletMovementPattern;
    }

    public FrontShot(float bulletSpeed, int damage) {
        this(bulletSpeed, damage, null);
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        // Angle: 90 degrees (Upward) for Player, 270 degrees (Downward) for Enemy
        float angle = isPlayer ? 90f : 270f;
        float rad = (float) Math.toRadians(angle);
        float vx = (float) (Math.cos(rad) * bulletSpeed);
        float vy = (float) (Math.sin(rad) * bulletSpeed);

        Bullet bullet;
        if (isPlayer) {
            bullet = bulletManager.spawnPlayerBullet(originX, originY, vx, vy, damage);
        } else {
            bullet = bulletManager.spawnEnemyBullet(originX, originY, vx, vy, damage);
        }

        if (bullet != null && bulletMovementPattern != null) {
            bullet.setMovementPattern(bulletMovementPattern);
        }
    }

    public float getBulletSpeed() { return bulletSpeed; }
    public void setBulletSpeed(float bulletSpeed) { this.bulletSpeed = bulletSpeed; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    public BulletMovementPattern getBulletMovementPattern() { return bulletMovementPattern; }
    public void setBulletMovementPattern(BulletMovementPattern bulletMovementPattern) { this.bulletMovementPattern = bulletMovementPattern; }
}
