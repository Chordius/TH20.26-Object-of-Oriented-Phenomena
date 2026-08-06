package com.netlab.frontend.commands;

import com.netlab.frontend.objects.Player;
import com.netlab.frontend.systems.BulletManager;

public class FocusCommand implements Command {
    private boolean focused;

    public FocusCommand(boolean focused) {
        this.focused = focused;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        player.setFocused(focused);
    }
}
