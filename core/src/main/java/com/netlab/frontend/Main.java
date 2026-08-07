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

        // 1. Centralized Asset Initialization (AssetManager Singleton)
        AssetManager.getInstance().init();

        // 2. Instantiate Player & Register GameHUD Observer
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
        inputHandler.handleInput(player, bulletManager, delta);

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
