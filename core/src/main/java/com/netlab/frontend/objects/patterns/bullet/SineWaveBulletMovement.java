package com.netlab.frontend.objects.patterns.bullet;

import com.netlab.frontend.objects.bullets.Bullet;

public class SineWaveBulletMovement implements BulletMovementPattern {
    private float frequency;
    private float amplitude;
    private float time = 0f;

    public SineWaveBulletMovement(float frequency, float amplitude) {
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    @Override
    public void move(Bullet bullet, float delta) {
        if (bullet != null) {
            time += delta;
            float rad = (float) Math.toRadians(bullet.getAngle());
            float baseDx = (float) Math.cos(rad) * bullet.getSpeed() * delta;
            float baseDy = (float) Math.sin(rad) * bullet.getSpeed() * delta;
            
            // Perpendicular sine wave oscillation offset
            float perpRad = rad + (float) (Math.PI / 2);
            float waveOffset = (float) Math.sin(time * frequency) * amplitude * delta;
            float waveDx = (float) Math.cos(perpRad) * waveOffset;
            float waveDy = (float) Math.sin(perpRad) * waveOffset;

            bullet.setX(bullet.getX() + baseDx + waveDx);
            bullet.setY(bullet.getY() + baseDy + waveDy);
        }
    }
}
