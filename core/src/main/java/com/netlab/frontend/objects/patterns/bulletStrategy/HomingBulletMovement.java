package com.netlab.frontend.objects.patterns.bulletStrategy;

import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.bullets.Bullet;

public class HomingBulletMovement implements BulletMovementPattern {
    private GameObject target;
    private float turnSpeed;

    public HomingBulletMovement(GameObject target, float turnSpeed) {
        this.target = target;
        this.turnSpeed = turnSpeed;
    }

    @Override
    public void move(Bullet bullet, float delta) {
        if (bullet != null) {
            if (target != null && !target.isDestroyed()) {
                float targetAngle = (float) Math.toDegrees(Math.atan2(
                        target.getY() - bullet.getY(),
                        target.getX() - bullet.getX()
                ));
                float diff = (targetAngle - bullet.getAngle()) % 360f;
                if (diff > 180f) diff -= 360f;
                if (diff < -180f) diff += 360f;

                bullet.setAngle(bullet.getAngle() + Math.signum(diff) * Math.min(Math.abs(diff), turnSpeed * delta));
            }

            float rad = (float) Math.toRadians(bullet.getAngle());
            float dx = (float) Math.cos(rad) * bullet.getSpeed() * delta;
            float dy = (float) Math.sin(rad) * bullet.getSpeed() * delta;
            bullet.setX(bullet.getX() + dx);
            bullet.setY(bullet.getY() + dy);
        }
    }
}
