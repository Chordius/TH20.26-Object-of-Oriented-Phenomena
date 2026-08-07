package com.netlab.frontend.objects.patterns.shootingStrategy;

import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.bullets.Bullet;
import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.netlab.frontend.systems.BulletManager;

public class HomingNeedleShot implements ShootingPattern {
    private float speed;
    private int numBullets;
    private int damage;
    private GameObject target;

    public HomingNeedleShot(float speed, int numBullets, int damage, GameObject target) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.damage = damage;
        this.target = target;
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        for (int h = 0; h < numBullets; h++) {
            float angle = (h % 2 == 0) ? 60f : 120f;
            float offsetX = (h % 2 == 0) ? -16f : 16f;
            Bullet bullet = isPlayer ?
                bulletManager.spawnPlayerBullet(originX + offsetX, originY, 0, speed, Math.max(5, damage / 2)) :
                bulletManager.spawnEnemyBullet(originX + offsetX, originY, 0, speed, Math.max(5, damage / 2));
            if (bullet != null && target != null) {
                bullet.setAngle(angle);
                bullet.setMovementPattern(new HomingBulletMovement(target, 180f));
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public int getNumBullets() { return numBullets; }
    public void setNumBullets(int numBullets) { this.numBullets = numBullets; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }
}
