package com.netlab.frontend.objects.patterns.shootingStrategy;

/**
 * Legacy alias for FrontShot.
 * Spawns bullets straight forward from the entity's nozzle.
 */
public class LinearShot extends FrontShot {
    public LinearShot(float bulletSpeed, int damage) {
        super(bulletSpeed, damage);
    }
}
