# Touhou OOP Practicum - Module 10: State Pattern & Facade Pattern

## 🎯 Purpose & Design Framework (Tujuan & Modul Design)

Module 10 explores level scripting, wave management, and subsystem simplification using two core Object-Oriented software design patterns:

1. **State Pattern**: Encapsulates wave phases and boss behavior stages into independent state objects (`WaveState`). State transitions are handled cleanly (`FairyWaveState` $\rightarrow$ `BossPhase1State` $\rightarrow$ `BossPhase2State`) without long `if-else` or `switch` statements in the main game loop.
2. **Facade Pattern**: Embeds `LevelWaveManager` as a central, high-level unified interface hiding the complexity of underlying subsystems (`EntityFactory`, `BulletManager`, `AssetManager`, and state transitions) from `Main.java`. `Main.java` simply calls `waveManager.update(delta)`.

---

## 👨‍💻 Student Implementation Expectations (Tugas Praktikan)

Practikans (students) are expected to implement and refactor the following core components in this module:

### 1. State Pattern (`com.netlab.frontend.states`)
- **`WaveState` Interface**:
  - `onEnter(LevelWaveManager manager)`: Triggered when entering a wave state (spawns entities, sets movement & shooting strategies).
  - `update(LevelWaveManager manager, float delta)`: Handles frame update logic and evaluates phase transition timers.
- **Concrete Wave States**:
  - **`FairyWaveState`**: Spawns Stage 1 Fairy and items; fires spread shots for 4 seconds before transitioning to `BossPhase1State`.
  - **`BossPhase1State`**: Moves Boss Cirno to Upper Left `(100, 420)` with `SpreadShot` strategy for 4 seconds before transitioning to `BossPhase2State`.
  - **`BossPhase2State`**: Moves Boss Cirno to Upper Right `(300, 420)` with `RingShot` strategy for 4 seconds before looping back to `BossPhase1State`.

### 2. Facade Pattern (`LevelWaveManager.java`)
- **Unified Level Facade**:
  - Centralizes enemy and item creation via Facade helper methods:
    ```java
    public Fairy spawnFairy(float x, float y, String name, int hp);
    public Boss spawnBoss(float x, float y, String name, int hp);
    public Item spawnItem(float x, float y, ItemType type);
    ```
  - Encapsulates state switching (`setState(WaveState newState)`) and drives active state updates (`update(delta)`).

### 3. Centralized Asset Initialization (`AssetManager.java`)
- **Singleton + Flyweight**:
  - Exposes `init()` to register all player animations, boss animations, enemy sprites, bullets, and drop items in a single place.

### 4. Main Game Loop Cleanup (`Main.java`)
- Refactors `create()` to delegate asset registration to `AssetManager.getInstance().init()` and level scripting to `LevelWaveManager`.

---

## 🏛️ Comprehensive Architecture & Pattern Map (Modules 6 – 10)

| Module | Design Pattern | Core Classes | Primary Purpose |
| :--- | :--- | :--- | :--- |
| **Module 6** | **Singleton & Flyweight** | `AssetManager` | Centralized texture cache & shared sprite instances. |
| **Module 7** | **Object Pool & Mediator** | `BulletManager`, `CollisionReferee` | Bullet recycling queue & decoupled collision resolution. |
| **Module 8** | **Command & Observer** | `InputHandler`, `GameHUD`, `GameObserver` | Input key encapsulation & real-time UI HUD state updates. |
| **Module 9** | **Strategy & Composite** | `ShootingPattern`, `CompositeShootingPattern`, `BulletMovementPattern` | Interchangeable shooting, homing, and bullet movement algorithms. |
| **Module 10** | **State & Facade** | `LevelWaveManager`, `WaveState` | Modular level scripting, wave state transitions & unified game loop facade. |

---

## 🧪 Verification & Testing Commands

To verify compilation and run the full practicum test suite (Modules 1 through 10):

```bash
./gradlew core:runTest
```
