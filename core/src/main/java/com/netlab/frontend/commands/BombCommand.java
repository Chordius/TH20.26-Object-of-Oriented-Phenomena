package com.netlab.frontend.commands;

import com.netlab.frontend.objects.Player;
import com.netlab.frontend.systems.BulletManager;

public class BombCommand implements Command {
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        player.useBomb(bulletManager);
    }
}
