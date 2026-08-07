package com.netlab.frontend.states;

import com.netlab.frontend.systems.LevelWaveManager;

public interface WaveState {
    void onEnter(LevelWaveManager manager);
    void update(LevelWaveManager manager, float delta);
}
