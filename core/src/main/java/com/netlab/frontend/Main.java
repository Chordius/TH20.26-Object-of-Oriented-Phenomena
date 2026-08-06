package com.netlab.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.netlab.frontend.systems.AssetManager;
import com.netlab.frontend.systems.EntityFactory;
import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.Player;
import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Player player;
    private Fairy fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    @Override
    public void create() {
        batch = new SpriteBatch();
        entities = new ArrayList<>();

        // 1. Dynamic Asset Registration (AssetManager - Singleton + Flyweight)
        AssetManager assets = AssetManager.getInstance();

        // --- Reimu (player.png: 32x48, 8 cols per row) ---
        // Row 0: Idle (Cols 0..3 start once, Cols 4..7 loop)
        assets.registerAnimationFromSheet("player_idle_start", "player.png", 32, 48, 0, 0, 4, 0.125f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("player_idle_loop",  "player.png", 32, 48, 0, 4, 4, 0.125f, Animation.PlayMode.LOOP);

        // Row 1: Moving Left (Cols 0..3 start once, Cols 4..7 loop)
        assets.registerAnimationFromSheet("player_left_start", "player.png", 32, 48, 1, 0, 4, 0.08f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("player_left_loop",  "player.png", 32, 48, 1, 4, 4, 0.12f, Animation.PlayMode.LOOP);

        // Row 2: Moving Right (Cols 0..3 start once, Cols 4..7 loop)
        assets.registerAnimationFromSheet("player_right_start", "player.png", 32, 48, 2, 0, 4, 0.08f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("player_right_loop",  "player.png", 32, 48, 2, 4, 4, 0.12f, Animation.PlayMode.LOOP);


        // --- Cirno (cirno.png: 64x64, 4 cols per row) ---
        // Row 0: Idle (Cols 0..1 start once, Cols 2..3 loop)
        assets.registerAnimationFromSheet("boss_idle_start", "cirno.png", 64, 64, 0, 0, 2, 0.2f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("boss_idle_loop",  "cirno.png", 64, 64, 0, 2, 2, 0.2f, Animation.PlayMode.LOOP);

        // Row 1: Moving Left (Cols 0..1 start once, Cols 2..3 loop)
        assets.registerAnimationFromSheet("boss_left_start", "cirno.png", 64, 64, 1, 0, 2, 0.15f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("boss_left_loop",  "cirno.png", 64, 64, 1, 2, 2, 0.15f, Animation.PlayMode.LOOP);

        // Row 2: Moving Right (Cols 0..1 start once, Cols 2..3 loop)
        assets.registerAnimationFromSheet("boss_right_start", "cirno.png", 64, 64, 2, 0, 2, 0.15f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("boss_right_loop",  "cirno.png", 64, 64, 2, 2, 2, 0.15f, Animation.PlayMode.LOOP);

        // Fairy Idle & Bullets
        assets.registerAnimationFromSheet("fairy_idle", "fairy.png", 32, 32, 1, 8, 0.125f);
        assets.registerRegionFromSheet("bullet_amulet", "bullets_small.png", 16, 16, 6, 0);
        assets.registerRegionFromSheet("bullet_danmaku", "bullets_small.png", 16, 16, 2, 0);

        // Correct items.png column coordinates (16x16 per cell):
        // Col 0: Small Power (P), Col 1: Point Item (点), Col 3: Bomb Item (B), Col 5: 1UP Item (1up)
        assets.registerRegionFromSheet("item_power", "items.png", 16, 16, 0, 0);
        assets.registerRegionFromSheet("item_point", "items.png", 16, 16, 0, 1);
        assets.registerRegionFromSheet("item_bomb", "items.png", 16, 16, 0, 3);
        assets.registerRegionFromSheet("item_life", "items.png", 16, 16, 0, 5);

        // 2. Instantiate entities via Factory Pattern (EntityFactory)
        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);
        fairy = EntityFactory.createFairy(150, 380, "Stage 1 Fairy", 20);
        boss = EntityFactory.createBoss(380, 400, "Cirno", 150);
        powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);

        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Check Player Bullet shooting input (Key Z)
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            entities.add(player.shootBullet());
        }

        // 2. Generic update & safe removal of off-screen/destroyed entities using updateAndClean<T>()
        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 3. Collision detection between active entities
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (!a.isDestroyed() && !b.isDestroyed()) {
                    if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                        a.onCollision(b);
                        b.onCollision(a);
                    }
                }
            }
        }

        // 4. Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 5. Render active entity sprites/animations with SpriteBatch
        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }
        batch.end();
    }

    // Generic Instance Method with Bounded Type Parameter <T extends GameObject>
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                System.out.println("Removed via Generic Iterator: " + entity.getClass().getSimpleName());
                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        AssetManager.getInstance().dispose();
    }
}
