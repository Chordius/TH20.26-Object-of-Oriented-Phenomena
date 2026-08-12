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

        // Touhou Dual Transition Condition:
        // Transition to BossPhase2State if Boss HP drops <= 125 (50% HP cutoff) OR Spell Card Timeout (6.0s)!
        boolean hpCutoffReached = (boss != null && boss.getHp() <= 125);
        if (hpCutoffReached || phaseTimer >= 6.0f) {
            if (hpCutoffReached) {
                System.out.println("[BossPhase1State] Boss Phase 1 HP threshold reached (<= 125 HP)!");
            } else {
                System.out.println("[BossPhase1State] Phase 1 Spell Card TIME OUT (6.0s)!");
            }
            // Clear bullets on phase transition (Authentic Touhou Spell Card clear)
            if (manager.getBulletManager() != null) {
                manager.getBulletManager().clearEnemyBullets(manager.getEntities());
            }
            manager.setState(new BossPhase2State());
        }
    }
}
