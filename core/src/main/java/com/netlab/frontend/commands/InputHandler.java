package com.netlab.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.netlab.frontend.objects.Player;
import com.netlab.frontend.systems.BulletManager;

public class InputHandler {

    public void handleInput(Player player, BulletManager bulletManager, float delta) {
        if (player == null || player.isDestroyed()) return;

        // 1. Focus Mode Toggle (Shift key)
        boolean focused = Gdx.input != null && (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT));
        Command focusCommand = new FocusCommand(focused);
        focusCommand.execute(player, bulletManager);

        // 2. Movement Commands (WASD or Arrow keys)
        float dx = 0;
        float dy = 0;
        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;
        }

        if (dx != 0 || dy != 0) {
            float speedMult = focused ? 0.45f : 1.0f; // Focus mode slows player for fine dodging
            Command moveCommand = new MoveCommand(dx * delta * speedMult, dy * delta * speedMult);
            moveCommand.execute(player, bulletManager);
        }

        // 3. Shoot Command (Z key)
        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            Command shootCommand = new ShootCommand();
            shootCommand.execute(player, bulletManager);
        }

        // 4. Bomb / Spell Card Command (X key)
        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            Command bombCommand = new BombCommand();
            bombCommand.execute(player, bulletManager);
        }
    }
}
