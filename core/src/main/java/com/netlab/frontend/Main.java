package com.netlab.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.netlab.frontend.commands.InputHandler;
import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.Player;
import com.netlab.frontend.systems.AssetManager;
import com.netlab.frontend.systems.BulletManager;
import com.netlab.frontend.systems.CollisionReferee;
import com.netlab.frontend.systems.EntityFactory;
import com.netlab.frontend.systems.LevelWaveManager;
import com.netlab.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private List<GameObject> entities;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private LevelWaveManager waveManager;
    private InputHandler inputHandler;
    private GameHUD gameHUD;

    @Override
    public void create() {
        batch = new SpriteBatch();
        entities = new ArrayList<>();
        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();
        inputHandler = new InputHandler();
        gameHUD = new GameHUD();

<<<<<<< HEAD
        // 1. Centralized Asset Initialization (AssetManager Singleton)
        AssetManager.getInstance().init();

        // 2. Instantiate Player & Register GameHUD Observer
=======
<<<<<<< HEAD
        // 1. Dynamic Asset Registration (AssetManager - Singleton + Flyweight)
        AssetManager assets = AssetManager.getInstance();

        // Reimu Animations (32x48 per tile, 8 columns)
        assets.registerAnimationFromSheet("player_idle", "player.png", 32, 48, 0, 0, 8, 0.125f, Animation.PlayMode.LOOP);
        assets.registerAnimationFromSheet("player_left_start", "player.png", 32, 48, 1, 0, 4, 0.8f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("player_left_loop",  "player.png", 32, 48, 1, 4, 4, 0.12f, Animation.PlayMode.LOOP);
        assets.registerAnimationFromSheet("player_right_start", "player.png", 32, 48, 2, 0, 4, 0.8f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("player_right_loop",  "player.png", 32, 48, 2, 4, 4, 0.12f, Animation.PlayMode.LOOP);

        // Boss Cirno Animations (64x64 per tile, 4 columns)
        assets.registerAnimationFromSheet("boss_idle", "cirno.png", 64, 64, 0, 0, 4, 0.2f, Animation.PlayMode.LOOP);
        assets.registerAnimationFromSheet("boss_left_start", "cirno.png", 64, 64, 1, 0, 2, 0.12f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("boss_left_loop",  "cirno.png", 64, 64, 1, 2, 2, 0.2f,  Animation.PlayMode.LOOP);
        assets.registerAnimationFromSheet("boss_right_start", "cirno.png", 64, 64, 2, 0, 2, 0.12f, Animation.PlayMode.NORMAL);
        assets.registerAnimationFromSheet("boss_right_loop",  "cirno.png", 64, 64, 2, 2, 2, 0.2f,  Animation.PlayMode.LOOP);

        // Stage 1 Fairy & Bullets
        assets.registerAnimationFromSheet("fairy_idle", "fairy.png", 32, 32, 1, 8, 0.125f);
        assets.registerRegionFromSheet("bullet_amulet", "bullets_small.png", 16, 16, 6, 0);
        assets.registerRegionFromSheet("bullet_danmaku", "bullets_small.png", 16, 16, 2, 0);

        // Items (items.png 16x16 per cell):
        assets.registerRegionFromSheet("item_power", "items.png", 16, 16, 0, 0);
        assets.registerRegionFromSheet("item_point", "items.png", 16, 16, 0, 1);
        assets.registerRegionFromSheet("item_bomb",  "items.png", 16, 16, 0, 3);
        assets.registerRegionFromSheet("item_life",  "items.png", 16, 16, 0, 5);
=======
        // 1. Centralized Asset Registration (AssetManager Singleton + Flyweight)
        AssetManager.getInstance().init();
>>>>>>> Modul-8

        // 2. Instantiate entities via Factory Pattern (EntityFactory)
>>>>>>> Modul-9
        player = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 8, 15, 3);
        player.registerObserver(gameHUD);
        entities.add(player);

        // 3. Instantiate LevelWaveManager Facade (Handles enemy spawning & wave states!)
        waveManager = new LevelWaveManager(entities, bulletManager, player);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Process Input via Command Pattern (InputHandler)
        inputHandler.handleInput(player, bulletManager, entities, delta);

        // 2. Level Wave Scripting & Enemy Scheduler (LevelWaveManager Facade + State Pattern)
        waveManager.update(delta);

        // 3. Generic update & safe removal of standard entities
        updateAndClean(entities, delta, 416, 560);

        // 4. Object Pool Update (BulletManager)
        bulletManager.update(delta, 416, 560);

        // 5. Mediator Pattern Collision Resolution (CollisionReferee - Core vs Graze detection)
        collisionReferee.resolveCollisions(player, entities, bulletManager);

        // 6. Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 7. Render active entity sprites, pooled bullets, and Observer UI HUD with SpriteBatch
        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }
        bulletManager.render(batch);
        gameHUD.render(batch);
        batch.end();

        // 8. Render UI Frame Lines & Focus Mode Core Hurtbox Indicator (Drawn ON TOP of sprites!)
        gameHUD.renderBackground();
        player.renderFocusIndicator(gameHUD.getShapeRenderer());
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
        if (gameHUD != null) {
            gameHUD.dispose();
        }
        AssetManager.getInstance().dispose();
    }
}
