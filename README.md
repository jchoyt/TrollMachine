# TrollMachine

A Bukkit/Spigot/Paper Minecraft plugin providing various non-game-changing
interruptions (`/troll`, `/8ball`, `/cookie`, `/joke`, random death messages,
sleep messages, and ManHunt integration).

## Building

Requires JDK 21 (a compatible Gradle toolchain is picked automatically if one
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
manual BuildTools step needed. The one dependency that *does* need to be
vendored by hand is the optional [ManHunt plugin](https://github.com/jchoyt/ManHunt)
(a soft dependency, used only for `ManHuntFailListener`); see `libs/README.md`.
`ManHunt.jar` isn't published anywhere — build it yourself from that project
and drop the resulting jar in `libs/`. If it's absent, the build falls back
to a compile-only stub so everything still builds; see `libs/README.md`.

## CI

Every push and pull request runs `./gradlew build` via GitHub Actions
(`.github/workflows/build.yml`) on JDK 21, using the ManHunt stub described
above. Dependabot (`.github/dependabot.yml`) opens a PR whenever a Gradle
dependency or a workflow's own GitHub Action has a newer version, weekly;
CI validates those PRs the same as any other.

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
- `libs/` — the one dependency that can't be fetched automatically (see above)
- `ci/manhunt-stub/` — compile-only ManHunt stand-in used when `libs/ManHunt.jar` is absent (never packaged into the plugin jar)
