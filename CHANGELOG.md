# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

##[1.0.1] 2022-08-28
## Changed
- Removed Budding Ametyst as a troll prize.  I thought that was the shard.

## [1.0.0] 2022-07-XX
### Added
- Added /troll command and first set of effects
- Updated to compile against 1.19

### Changed
- Changed the special welcome messages
- Got to an even 100 death death_messages
- Better list of ManHunt non-participation prizes. Added some slightly useful things and dropped some repetitive prizes.

### Removed

## [Unreleased]
### Added
- Player gets crappy gift when they fail to register their target status with ManHunt
- Custom sleep message from TM when a player sleeps

### Changed
- Dependency on [ManHunt plugin](https://github.com/jchoyt/ManHunt) version 1.2 or later (for AfkEvent)
- /jokes works when kaityizzy is online, but it won't use the blue jokes listener
- Replaced the Ant build with Gradle; spigot-api is now resolved from Spigot's Maven repo instead of built/vendored by hand. See README.md.
- Added GitHub Actions CI to build and test on every push/PR.
- Added Dependabot for build.gradle dependencies and GitHub Actions versions.
- ManHunt is now resolved as a real dependency via JitPack instead of a
  hand-vendored jar (with a compile-only stub fallback in CI). Since ManHunt
  itself now requires spigot-api 26.2 / Java 25, this plugin's own target
  bumped from spigot-api 1.21 / Java 17 to match; requires JDK 25 to build
  and a matching server version to run. Gradle wrapper bumped 8.14.3 → 9.7.0
  accordingly.

### Fixed
- permission for /franklin was munged by a misplaced copypasta

### Removed
