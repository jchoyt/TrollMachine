# CONTENTS
Developer must acquire the following files to run `ant test`. They are plain
downloads from Maven Central (no build required):

 * junit-4.13.2.jar
 * hamcrest-core-1.3.jar
 * mockito-core-5.16.0.jar
 * byte-buddy-1.15.11.jar
 * byte-buddy-agent-1.15.11.jar
 * objenesis-3.3.jar

Fetch them all with:

```
cd test-lib
BASE=https://repo1.maven.org/maven2
curl -sSL -O "$BASE/junit/junit/4.13.2/junit-4.13.2.jar"
curl -sSL -O "$BASE/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
curl -sSL -O "$BASE/org/mockito/mockito-core/5.16.0/mockito-core-5.16.0.jar"
curl -sSL -O "$BASE/net/bytebuddy/byte-buddy/1.15.11/byte-buddy-1.15.11.jar"
curl -sSL -O "$BASE/net/bytebuddy/byte-buddy-agent/1.15.11/byte-buddy-agent-1.15.11.jar"
curl -sSL -O "$BASE/org/objenesis/objenesis/3.3/objenesis-3.3.jar"
```

These are only needed to run the test suite (`ant test`); they are not
required to build the plugin jar (`ant jar`).

Note: tests avoid constructing TrollCommandExecutor's real troll effects
(Cobweb, SetPotionEffect, etc.) because doing so triggers a one-time
class-load of Bukkit's Sound/PotionEffectType registries, which requires a
live server and isn't reproducible with plain Mockito stubs. Tests use
lightweight TrollExecutor stubs instead (see StubTrollExecutor) — see the
package-private TrollCommandExecutor(TrollMachine, List) constructor.
