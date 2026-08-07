package com.netlab.frontend.objects.patterns;

import com.netlab.frontend.systems.BulletManager;

public class SpreadShot implements ShootingPattern {
    private float speed;
    private int numBullets;
    private float spreadAngleDegrees;
    private int damage;

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees) {
        this(speed, numBullets, spreadAngleDegrees, 1);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.spreadAngleDegrees = spreadAngleDegrees;
        this.damage = damage;
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        // Base direction angle: 90 degrees (Upward) for Player, 270 degrees (Downward) for Enemy
        float centerAngle = isPlayer ? 90f : 270f;
        float startAngle = centerAngle - (spreadAngleDegrees / 2f);
        float angleStep = (numBullets > 1) ? (spreadAngleDegrees / (numBullets - 1)) : 0;

        int bulletDamage = isPlayer ? 25 : this.damage;

        for (int i = 0; i < numBullets; i++) {
            float angleDeg = startAngle + (i * angleStep);
            float angleRad = (float) Math.toRadians(angleDeg);

            float vx = (float) (speed * Math.cos(angleRad));
            float vy = (float) (speed * Math.sin(angleRad));

            if (isPlayer) {
                bulletManager.spawnPlayerBullet(originX, originY, vx, vy, bulletDamage);
            } else {
                bulletManager.spawnEnemyBullet(originX, originY, vx, vy, bulletDamage);
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public int getNumBullets() { return numBullets; }
    public void setNumBullets(int numBullets) { this.numBullets = numBullets; }

    public float getSpreadAngleDegrees() { return spreadAngleDegrees; }
    public void setSpreadAngleDegrees(float spreadAngleDegrees) { this.spreadAngleDegrees = spreadAngleDegrees; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
}
