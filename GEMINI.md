# GEMINI.md

This file provides guidance and project memory for Antigravity when working with code in this repository.

## What this is

TrollMachine is a Bukkit/Spigot/Paper Minecraft plugin providing various non-game-changing interruptions (`/troll`, `/8ball`, `/cookie`, `/joke`, `/wolverine`, random death messages, sleep messages, DM blocking, and ManHunt integration).

## Build & Test Commands

Requires JDK 25 (a compatible Gradle toolchain is picked automatically if one is already installed).

```bash
./gradlew build   # compile, run tests, produce build/libs/TrollMachine-<version>.jar
./gradlew test    # run unit tests
./gradlew jar     # build plugin jar, skipping tests
./gradlew clean   # remove build output
```

Run a single test class or test method:

```bash
./gradlew test --tests TrollCommandExecutorTest
./gradlew test --tests "TrollCommandExecutorTest.registerTroll_startsAtOneForANewSender"
```

If `./gradlew` fails to start because default `java` is newer than Gradle supports:
`JAVA_HOME=/path/to/an/older/jdk ./gradlew build`.

### Versioning & Dependencies

- Plugin version is set in `build.gradle` (`version = '26.3'`) and templated into `plugin.yml` at build time via `processResources`; do not hardcode the version in `plugin.yml`.
- `spigot-api` is resolved from Spigot's Maven repository (`https://hub.spigotmc.org/nexus/content/repositories/snapshots/`).
- Optional soft dependency on `ManHunt` (`com.github.jchoyt:ManHunt:master-SNAPSHOT`) is resolved via JitPack.
- CI (`.github/workflows/build.yml`) builds and tests with JDK 25 on every push/PR. Dependabot (`.github/dependabot.yml`) provides weekly dependency updates.

## Architecture & Features

### Core Plugin
- `TrollMachine` (`TrollMachine.java`) is the `JavaPlugin` entry point. In `onEnable()`, it registers commands and listeners.
- Contains UUIDs for specific players: `kaitlynUuid`, `entomoUuid`, `gooberUuid`, `duplocookUuid`, and a `youngins` list (`kaitlynUuid`, `gooberUuid`, `duplocookUuid`).
- Shared utilities: `loadFile`/`loadResource` (reads flavor-text files), `sendQuote` (splits long messages for Minecraft's 256-char limit), `asyncBroadcast`, `runCommand`, and `areYounginsOnline()`.

### Commands
- `/troll <player>` (`TrollCommandExecutor`): Applies a random effect (`Cobweb`, `Lightning`, `SetEffect`, `SetPotionEffect`, `PlaySound`). Tracks recent troll frequency with 5-minute cooldown decay; rolls escalating backfire probability. Supports injectable `List<TrollExecutor>` for unit testing.
- `/joke` (`JokeCommandExecutor`): Broadcasts jokes. Gated by `plugin.areYounginsOnline()`: if any youngin is online, only clean `one-liners.txt` jokes are used; otherwise, `allJokes` (`one-liners.txt` + `blue-one-liners.txt`) are available.
- `/cookie` (`FortuneCommandExecutor`): Reads fortunes from `fortunes.txt`.
- `/8ball <question>` (`EightBallCommandExecutor`): Answers yes/no questions using `8ball.txt`.
- `/wolverine` (`WolverineCommandExecutor`): Granted to `allowedUsers` (`kaitlynUuid`, `gooberUuid`, `duplocookUuid`) giving Regeneration effect for 10 seconds; any unauthorized user receives Nausea and hunger depletion.

### Listeners
- `PlayerChatListener`: Welcomes joining players with special messages (`special_welcome.txt`). Intercepts private message commands (`/msg`, `/tell`, `/w`, `/whisper`) targeting blocked players (`kaitlynUuid`) and cancels them with a notice.
- `PlayerDeathListener`: Broadcasts custom death flavor text (`death_messages.txt`).
- `SleepListener`: Wishes players good night when entering bed (`special_welcome.txt`).
- `ManHuntFailListener`: Soft dependency on ManHunt plugin; listens for `AfkEvent` and awards a random consolation prize from `NonParticipationPrizes`.

### Testing Seams
- Tests use JUnit 4 + Mockito with mocked Bukkit types (`mock(TrollMachine.class)`, etc.).
- Package-private seams are intentionally exposed for testability without a live Bukkit server:
  - `TrollMachine.random` for deterministic pseudo-random control
  - `TrollMachine.loadResource` wrapping protected `JavaPlugin#getTextResource`
  - `TrollCommandExecutor` constructor accepting `List<TrollExecutor>` (`StubTrollExecutor`)
  - `FixedRandom` test helper

## Things to Revisit

- **Claude Memory Files (Pending Final Deletion Review)**:
  - Verify all information has been fully transferred before deleting:
    - `CLAUDE.md` (project root)
    - `.claude/settings.local.json` and `.claude/` (project root)
    - `~/.claude/projects/-home-jchoyt-devel-TrollMachine/` (`memory/MEMORY.md` and session logs `bfbaeb70-a5f3-4abd-8e15-ecd955cd2a29.jsonl`, `b7d8e420-8d3c-4f24-96e3-a2dba61dcac3.jsonl`)
- **PlayerChatListener DM Blocking**:
  - The commented-out test line `// blockedPlayers.add(plugin.entomoUuid);` is currently kept in `PlayerChatListener.java`.
  - Revisit whether `blockedPlayers` should dynamically track all players in `plugin.youngins` or continue using individual additions (`kaitlynUuid`, `gooberUuid`).
  - Add unit tests for `PlayerChatListener`'s `PlayerCommandPreprocessEvent` message intercepting.
- **ManHunt Dependency Tagging**:
  - Currently floating on `master-SNAPSHOT` via JitPack (`com.github.jchoyt:ManHunt:master-SNAPSHOT`). Revisit to pin to a stable tag once cut upstream.
