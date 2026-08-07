package com.netlab.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.netlab.frontend.systems.BulletManager;

public abstract class EntityShooter extends GameObject {
    protected ShootingPattern shootingPattern;
    protected EntityMovementPattern movementPattern;

    public EntityShooter(float x, float y, float width, float height, float speed, Color color) {
        super(x, y, width, height, speed, color);
    }

    public EntityShooter(float x, float y, float width, float height, float speed, Color color,
                         ShootingPattern shootingPattern, EntityMovementPattern movementPattern) {
        super(x, y, width, height, speed, color);
        this.shootingPattern = shootingPattern;
        this.movementPattern = movementPattern;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updateMovement(delta);
    }

    // Strategy Pattern Execution: Entity Movement
    public void updateMovement(float delta) {
        if (movementPattern != null && active) {
            movementPattern.move(this, delta);
        }
    }

    // Strategy Pattern Execution: Uniform shootBullet method across Player, Fairy, Boss, and all EntityShooter subclasses
    public void shootBullet(BulletManager bulletManager) {
        if (shootingPattern != null && active && !isDestroyed()) {
            boolean isPlayer = (this instanceof Player);
            shootingPattern.execute(x + width / 2f - 8f, y + (isPlayer ? height : 0f), bulletManager, isPlayer);
        }
    }

    // Dependency Injection & Dynamic Strategy Swapping
    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }

    public EntityMovementPattern getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(EntityMovementPattern movementPattern) {
        this.movementPattern = movementPattern;
    }
}
