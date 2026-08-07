package com.netlab.frontend.states;

import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.items.ItemType;
import com.netlab.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.netlab.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.netlab.frontend.systems.LevelWaveManager;

public class FairyWaveState implements WaveState {
    private float waveTimer = 0f;
    private float shootTimer = 0f;

    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("[WaveState] Switched to FairyWaveState (Stage 1 Fairy Wave)");
        Fairy fairy = manager.getFairy();
        if (fairy == null || fairy.isDestroyed()) {
            fairy = manager.spawnFairy(150, 380, "Stage 1 Fairy", 20);
        }
        fairy.setMovementPattern(new LinearEntityMovement(30f, -10f));
        fairy.setShootingPattern(new SpreadShot(150f, 3, 20f, 10));
    }

    @Override
    public void update(LevelWaveManager manager, float delta) {
        waveTimer += delta;
        shootTimer += delta;

        Fairy fairy = manager.getFairy();
        if (fairy != null && !fairy.isDestroyed()) {
            if (shootTimer >= 1.5f) {
                fairy.shootBullet(manager.getBulletManager());
                shootTimer = 0f;
            }
        }

        // After 4.0 seconds, transition state to Boss Phase 1!
        if (waveTimer >= 4.0f) {
            manager.setState(new BossPhase1State());
        }
    }
}
