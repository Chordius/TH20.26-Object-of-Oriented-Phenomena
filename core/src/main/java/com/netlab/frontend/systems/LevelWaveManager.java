package com.netlab.frontend.systems;

import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.Player;
import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;
import com.netlab.frontend.states.FairyWaveState;
import com.netlab.frontend.states.WaveState;

import java.util.ArrayList;
import java.util.List;

/**
 * Facade & Level Scripting Manager (Module 10: Facade Pattern & State Pattern)
 * 
 * Provides a unified high-level Facade interface for Main.java to drive
 * enemy spawning, wave progression, and state transitions without Main needing
 * to manually coordinate underlying subsystems (Factory, Object Pool, Strategy).
 */
public class LevelWaveManager {
    private List<GameObject> entities;
    private BulletManager bulletManager;
    private Player player;
    private Boss boss;
    private Fairy fairy;
    private List<Fairy> fairies = new ArrayList<>();

    private WaveState currentState;

    public LevelWaveManager(List<GameObject> entities, BulletManager bulletManager, Player player) {
        this.entities = entities;
        this.bulletManager = bulletManager;
        this.player = player;
        
        // Initial state: FairyWaveState
        setState(new FairyWaveState());
    }

    public LevelWaveManager(List<GameObject> entities, BulletManager bulletManager, Boss boss, Fairy fairy) {
        this.entities = entities;
        this.bulletManager = bulletManager;
        this.boss = boss;
        this.fairy = fairy;
        if (fairy != null) {
            fairies.add(fairy);
        }
        
        // Initial state: FairyWaveState
        setState(new FairyWaveState());
    }

    // Facade Entity Spawning Helper Methods
    public Fairy spawnFairy(float x, float y, String name, int hp) {
        Fairy f = EntityFactory.createFairy(x, y, name, hp);
        if (entities != null && !entities.contains(f)) {
            entities.add(f);
        }
        if (!fairies.contains(f)) {
            fairies.add(f);
        }
        this.fairy = f; // Stored as primary/latest fairy for backwards compatibility
        return f;
    }

    public Boss spawnBoss(float x, float y, String name, int hp) {
        Boss b = EntityFactory.createBoss(x, y, name, hp);
        if (entities != null && !entities.contains(b)) {
            entities.add(b);
        }
        this.boss = b;
        return b;
    }

    public Item spawnItem(float x, float y, ItemType type) {
        Item item = EntityFactory.createItem(x, y, type);
        if (entities != null && !entities.contains(item)) {
            entities.add(item);
        }
        return item;
    }

    public boolean areAllFairiesDefeated() {
        if (fairies == null || fairies.isEmpty()) return true;
        for (Fairy f : fairies) {
            if (f != null && !f.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    public void setState(WaveState newState) {
        if (newState != null) {
            this.currentState = newState;
            this.currentState.onEnter(this);
        }
    }

    // High-Level Facade Update Method called by Main.java
    public void update(float delta) {
        if (currentState != null) {
            currentState.update(this, delta);
        }
    }

    // Facade Accessors & Encapsulation
    public List<GameObject> getEntities() { return entities; }
    public BulletManager getBulletManager() { return bulletManager; }
    public Player getPlayer() { return player; }
    public Boss getBoss() { return boss; }
    public Fairy getFairy() { return fairy; }
    public List<Fairy> getFairies() { return fairies; }
    public WaveState getCurrentState() { return currentState; }
}
