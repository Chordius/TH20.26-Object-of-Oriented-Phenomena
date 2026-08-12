package com.netlab.frontend.states;

import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.netlab.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.netlab.frontend.systems.LevelWaveManager;

import java.util.List;

public class FairyWaveState implements WaveState {
    private float waveTimer = 0f;
    private float shootTimer = 0f;

    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("[WaveState] Switched to FairyWaveState (Stage 1 Multi-Fairy Wave)");
        
        // Spawn a formation of 3 Stage 1 Fairies!
        Fairy fairy1 = manager.spawnFairy(100, 420, "Stage 1 Fairy Left", 20);
        fairy1.setMovementPattern(new LinearEntityMovement(20f, -10f));
        fairy1.setShootingPattern(new SpreadShot(150f, 3, 20f, 10));

        Fairy fairy2 = manager.spawnFairy(200, 450, "Stage 1 Fairy Center", 20);
        fairy2.setMovementPattern(new LinearEntityMovement(0f, -15f));
        fairy2.setShootingPattern(new SpreadShot(150f, 3, 20f, 10));

        Fairy fairy3 = manager.spawnFairy(300, 420, "Stage 1 Fairy Right", 20);
        fairy3.setMovementPattern(new LinearEntityMovement(-20f, -10f));
        fairy3.setShootingPattern(new SpreadShot(150f, 3, 20f, 10));
    }

    @Override
    public void update(LevelWaveManager manager, float delta) {
        waveTimer += delta;
        shootTimer += delta;

        // Iterate through all active fairies in the wave formation
        List<Fairy> fairies = manager.getFairies();
        if (fairies != null) {
            for (Fairy fairy : fairies) {
                if (fairy != null && !fairy.isDestroyed()) {
                    if (shootTimer >= 1.5f) {
                        fairy.shootBullet(manager.getBulletManager());
                    }
                }
            }
        }
        if (shootTimer >= 1.5f) {
            shootTimer = 0f;
        }

        // After 4.0 seconds, transition state to Boss Phase 1!
        if (waveTimer >= 4.0f) {
            manager.setState(new BossPhase1State());
        }
    }
}
