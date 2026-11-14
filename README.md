# melody-http

HTTP helper in `com.distantmelody.library.service`. `ServiceUtil` and the `SU*` types (`SUError`, `SUHeader`, `SUMethod`, `SUOption`, `SUProxy`, `SUResponseCode`, `SUResponseHeaders`, `SUTimeout`) travel together. The only library dependency is `com.distantmelody:melody-json`.

```xml
<dependency>
  <groupId>com.distantmelody</groupId>
  <artifactId>melody-http</artifactId>
  <version>0.1.0</version>
</dependency>
```

`melody-json` is a compile dependency, so a project that depends on `melody-http` can import `com.distantmelody.library.json.J` without declaring `melody-json` itself.

```
mvn test
```

Apache License 2.0. See [LICENSE](LICENSE).

Publishing `0.1.0` to Maven Central uses `mvn -Prelease deploy` after `com.distantmelody:melody-json:0.1.0` is already on Central, the `com.distantmelody` namespace is verified, a Central Portal token is in `settings.xml` under server id `central`, and GPG signing works. That deploy has not been run.
