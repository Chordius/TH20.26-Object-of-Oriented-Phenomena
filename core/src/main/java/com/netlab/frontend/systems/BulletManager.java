package com.netlab.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.bullets.Bullet;
import com.netlab.frontend.objects.bullets.BulletType;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;
import com.netlab.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BulletManager {
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    public BulletManager() {
        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();
    }

    // Object Pool Spawning for Player Bullets via EntityFactory
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll(); // Recycles pooled object
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage); // Factory Pattern
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    // Spawns 8 giant homing Fantasy Seal spirit orbs for Reimu's Bomb (Spirit Sign "Fantasy Seal")
    public void spawnFantasySealOrbs(float originX, float originY, GameObject targetEnemy) {
        int orbCount = 8;
        float radius = 30f;
        float speed = 350f;
        int orbDamage = 35; // 35 damage x 8 orbs = 280 total spell card damage potential!

        for (int i = 0; i < orbCount; i++) {
            float angleDeg = i * (360f / orbCount);
            float angleRad = (float) Math.toRadians(angleDeg);
            float spawnX = originX + (float) Math.cos(angleRad) * radius - 8f;
            float spawnY = originY + (float) Math.sin(angleRad) * radius - 8f;

            float vx = (float) Math.cos(angleRad) * 100f;
            float vy = (float) Math.sin(angleRad) * 100f;

            Bullet orb = spawnPlayerBullet(spawnX, spawnY, vx, vy, orbDamage);
            orb.setMovementPattern(new HomingBulletMovement(targetEnemy, 250f));
        }
        System.out.println("[BulletManager] Spawned 8 homing Fantasy Seal Spirit Orbs!");
    }

    // Object Pool Spawning for Enemy Bullets via EntityFactory
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll(); // Recycles pooled object
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage); // Factory Pattern
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        // Safe update & recycling of player bullets using Iterator
        Iterator<Bullet> pIter = activePlayerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.destroy();
                pIter.remove();
                playerBulletPool.offer(bullet); // Return to pool!
            }
        }

        // Safe update & recycling of enemy bullets using Iterator
        Iterator<Bullet> eIter = activeEnemyBullets.iterator();
        while (eIter.hasNext()) {
            Bullet bullet = eIter.next();
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.destroy();
                eIter.remove();
                enemyBulletPool.offer(bullet); // Return to pool!
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
    }

    // Bomb / Spell Card execution: Clear active enemy bullets and convert to drop items!
    public void clearEnemyBullets(List<GameObject> entities) {
        Iterator<Bullet> eIter = activeEnemyBullets.iterator();
        while (eIter.hasNext()) {
            Bullet bullet = eIter.next();
            if (entities != null) {
                Item item = EntityFactory.createItem(bullet.getX(), bullet.getY(), ItemType.POINT);
                entities.add(item);
            }
            bullet.destroy();
            eIter.remove();
            enemyBulletPool.offer(bullet); // Recycles back to pool!
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    // Dynamic Getters & Clean Recycling Methods for Testing
    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public Queue<Bullet> getPlayerBulletPool() { return playerBulletPool; }
    public Queue<Bullet> getEnemyBulletPool() { return enemyBulletPool; }
    public int getPlayerPoolSize() { return playerBulletPool.size(); }
    public int getEnemyPoolSize() { return enemyBulletPool.size(); }
}
