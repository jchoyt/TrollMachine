# CONTENTS

Spigot's own API (`org.spigotmc:spigot-api`) is now resolved automatically by
Gradle from Spigot's Maven repository — no manual download needed.

The one thing that still has to be vendored here by hand is:

 * `ManHunt.jar` — the optional [ManHunt plugin](https://github.com/jchoyt/ManHunt)
   (version 1.2+), used only as a soft dependency (see `softdepend` in
   `src/main/resources/plugin.yml`). It isn't published to any Maven repo, so
   there's no way for Gradle to fetch it automatically. Build it yourself
   with `ant jar` (or Gradle, if that project has since migrated too) and
   drop the resulting jar here.

   If this file is missing, `build.gradle` automatically falls back to a
   tiny compile-only stub (`ci/manhunt-stub/`) so the project still builds
   and the test suite still runs — that's how CI does it. The stub is never
   packaged into the plugin jar, so a build without the real jar just means
   `ManHuntFailListener` won't actually work against a live ManHunt plugin;
   everything else is unaffected.
