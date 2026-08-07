package com.netlab.frontend.states;

import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.netlab.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.netlab.frontend.systems.LevelWaveManager;

public class BossPhase1State implements WaveState {
    private float phaseTimer = 0f;
    private float shootTimer = 0f;

    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("[WaveState] Switched to BossPhase1State: Cirno moving Upper Left (100, 420) with SpreadShot!");
        Boss boss = manager.getBoss();
        if (boss == null || boss.isDestroyed()) {
            boss = manager.spawnBoss(250, 400, "Cirno", 250);
            if (manager.getPlayer() != null) {
                manager.getPlayer().setTargetEnemy(boss);
            }
        }
        boss.setMovementPattern(new TargetPointMovement(100f, 420f, 200f));
        boss.setShootingPattern(new SpreadShot(200f, 3, 30f, 15));
    }

    @Override
    public void update(LevelWaveManager manager, float delta) {
        phaseTimer += delta;
        shootTimer += delta;

        Boss boss = manager.getBoss();
        if (boss != null && !boss.isDestroyed()) {
            if (shootTimer >= 1.5f) {
                boss.shootBullet(manager.getBulletManager());
                shootTimer = 0f;
            }
        }

        // After 4.0 seconds, transition state to Boss Phase 2!
        if (phaseTimer >= 4.0f) {
            manager.setState(new BossPhase2State());
        }
    }
}
