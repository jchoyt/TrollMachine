# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

TrollMachine is a Bukkit/Spigot/Paper Minecraft plugin providing various
non-game-changing interruptions (`/troll`, `/8ball`, `/cookie`, `/joke`,
random death messages, sleep messages, and ManHunt integration).

## Commands

Requires JDK 25 (a compatible Gradle toolchain is picked automatically if
one is already installed).

```
./gradlew build   # compile, run tests, produce build/libs/TrollMachine-<version>.jar
./gradlew test    # just run the tests
./gradlew jar     # just build the plugin jar, skipping tests
./gradlew clean   # remove build output
```

Run a single test class or method with `--tests`:

```
./gradlew test --tests TrollCommandExecutorTest
./gradlew test --tests "TrollCommandExecutorTest.registerTroll_startsAtOneForANewSender"
```

If `./gradlew` fails to *start* (not just fails to compile) because the
default `java` is newer than Gradle supports, point it at an older installed
JDK: `JAVA_HOME=/path/to/an/older/jdk ./gradlew build`.

The plugin version is set in one place — the `version` property near the
top of `build.gradle` — and templated into `plugin.yml` at build time via
`processResources`; don't set the version in `plugin.yml` directly.

`spigot-api` is resolved automatically from Spigot's Maven repository, and
the optional [ManHunt plugin](https://github.com/jchoyt/ManHunt) (a soft
dependency, used only by `ManHuntFailListener`) is resolved via
[JitPack](https://jitpack.io), which builds ManHunt's Gradle project
straight from GitHub. ManHunt has no tagged release since migrating to
Gradle, so this floats on `com.github.jchoyt:ManHunt:master-SNAPSHOT` for
now — bump `build.gradle` to a real tag once one exists upstream.

CI (`.github/workflows/build.yml`) runs `./gradlew build` on JDK 25 for
every push/PR. Dependabot (`.github/dependabot.yml`) opens weekly PRs for
Gradle dependency and GitHub Action updates; CI validates those the same as
any other PR.

## Architecture

- `TrollMachine` (`TrollMachine.java`) is the `JavaPlugin` entry point.
  `onEnable()` wires up every feature by constructing one listener/command
  executor per feature and registering it with Bukkit — there's no shared
  registry or DI container, just direct `new Xyz(this)` calls. It also owns
  small shared utilities used across features: `loadFile`/`loadResource`
  (reads a flavor-text resource into a `List<String>`), `sendQuote` (picks
  and broadcasts a random line, splitting on the 256-char chat limit), and
  `asyncBroadcast`/`runCommand` (schedule work via `BukkitScheduler`).
  `ManHuntFailListener` is only constructed if the ManHunt plugin is
  actually loaded on the server (`getPluginManager().getPlugin("ManHunt")`),
  since it's a soft dependency.

- Each feature is a self-contained `Listener` or `CommandExecutor` in
  `com.asharpminer.trollmachine` (e.g. `PlayerChatListener`,
  `PlayerDeathListener`, `SleepListener`, `FortuneCommandExecutor`,
  `JokeCommandExecutor`, `EightBallCommandExecutor`) that takes the
  `TrollMachine` instance in its constructor and typically loads its own
  flavor-text file (`src/main/resources/*.txt`) via `plugin.loadFile(...)`.

- `/troll` (`TrollCommandExecutor`) is the most involved feature: it picks
  a random effect from a pluggable list of `TrollExecutor` implementations
  (`com.asharpminer.trollmachine.trolls` — `Cobweb`, `Lightning`,
  `SetEffect`, `SetPotionEffect`, `PlaySound`) and applies it to the target
  player. It also tracks a per-player "trolled recently" count
  (`registerTroll`, decaying one step per 5-minute cooldown window) that
  drives escalating odds of the troll *backfiring* onto the sender instead
  (`rollBackfire`). `TrollCommandExecutor`'s constructor accepts an
  injectable `List<TrollExecutor>`, used by tests to swap in
  `StubTrollExecutor` instead of the real effects (which need a live Bukkit
  server for their `Sound`/`PotionEffectType` registries).

- `ManHuntFailListener` reacts to ManHunt's `AfkEvent` (fired when a target
  fails to register as such) and awards a random "crap prize" from
  `NonParticipationPrizes` to soften the failure.

- Tests are plain JUnit 4 + Mockito against mocked Bukkit types (`mock(TrollMachine.class)`,
  etc.) — no MockBukkit or live server. Several classes expose
  package-private constructors/fields specifically as test seams (e.g.
  `TrollMachine.random`, `TrollCommandExecutor`'s
  `List<TrollExecutor>`-accepting constructor, `TrollMachine.loadResource`
  wrapping the protected `JavaPlugin#getTextResource`) — preserve these
  seams rather than mocking Bukkit statics when adding tests.

- Flavor text lives in `src/main/resources/*.txt` (one entry per line) and
  is loaded at runtime, not baked into code — add new jokes/fortunes/etc.
  there rather than in Java source.
