# TrollMachine

A Bukkit/Spigot/Paper Minecraft plugin providing various non-game-changing
interruptions (`/troll`, `/8ball`, `/cookie`, `/joke`, random death messages,
sleep messages, and ManHunt integration).

## Building

Requires JDK 25 (a compatible Gradle toolchain is picked automatically if one
is already installed; nothing needs to be pre-installed by hand beyond that).

```
./gradlew build
```

This compiles the plugin, runs the test suite, and produces
`build/libs/TrollMachine-<version>.jar`.

The plugin version is set in one place — the `version` property near the top
of `build.gradle` — and templated into `plugin.yml` at build time; don't set
it in `plugin.yml` directly.

Other useful tasks:

```
./gradlew test   # just run the tests
./gradlew jar    # just build the plugin jar, skipping tests
./gradlew clean  # remove build output
```

`spigot-api` is resolved automatically from Spigot's Maven repository — no
manual BuildTools step needed. The optional [ManHunt plugin](https://github.com/jchoyt/ManHunt)
(a soft dependency, used only for `ManHuntFailListener`) is resolved
automatically too, via [JitPack](https://jitpack.io), which builds ManHunt's
own Gradle project straight from GitHub. ManHunt hasn't cut a tagged release
since migrating to Gradle, so this floats on `master-SNAPSHOT` for now —
bump `build.gradle` to a real tag once ManHunt has one.

## CI

Every push and pull request runs `./gradlew build` via GitHub Actions
(`.github/workflows/build.yml`) on JDK 25. Dependabot
(`.github/dependabot.yml`) opens a PR whenever a Gradle dependency or a
workflow's own GitHub Action has a newer version, weekly; CI validates those
PRs the same as any other.

### If your default `java` is newer than Gradle supports

Gradle occasionally lags a few months behind the newest JDK release. If
`./gradlew` fails to start at all (rather than failing to compile), point it
at an older installed JDK instead:

```
JAVA_HOME=/path/to/an/older/jdk ./gradlew build
```

## Project layout

Standard Gradle/Maven layout:
- `src/main/java` — plugin source
- `src/main/resources` — `plugin.yml` and the flavor-text files it loads at runtime
- `src/test/java` — unit tests (JUnit 4 + Mockito)
