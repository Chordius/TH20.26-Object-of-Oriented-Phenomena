package com.netlab.frontend.states;

import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.netlab.frontend.objects.patterns.shootingStrategy.RingShot;
import com.netlab.frontend.systems.LevelWaveManager;

public class BossPhase2State implements WaveState {
    private float phaseTimer = 0f;
    private float shootTimer = 0f;

    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("[WaveState] Switched to BossPhase2State: Cirno moving Upper Right (300, 420) with RingShot!");
        Boss boss = manager.getBoss();
        if (boss == null || boss.isDestroyed()) {
            boss = manager.spawnBoss(250, 400, "Cirno", 250);
            if (manager.getPlayer() != null) {
                manager.getPlayer().setTargetEnemy(boss);
            }
        }
        boss.setMovementPattern(new TargetPointMovement(300f, 420f, 200f));
        boss.setShootingPattern(new RingShot(180f, 8, 10));
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

        // Touhou Dual Transition Condition:
        // Final phase completes if Boss HP <= 0 OR Spell Card Timeout (6.0s)!
        boolean bossDefeated = (boss != null && boss.getHp() <= 0);
        if (bossDefeated || phaseTimer >= 6.0f) {
            if (bossDefeated) {
                System.out.println("[BossPhase2State] Boss Cirno Defeated! STAGE CLEARED!");
            } else {
                System.out.println("[BossPhase2State] Phase 2 Spell Card TIME OUT (6.0s)! STAGE CLEARED!");
            }
            // Clear remaining enemy bullets into items
            if (manager.getBulletManager() != null) {
                manager.getBulletManager().clearEnemyBullets(manager.getEntities());
            }
            // Loop back to FairyWaveState for continuous gameplay loops
            manager.setState(new FairyWaveState());
        }
    }
}
