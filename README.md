# API Hammer

### Prerequisite
```
Gradle version: 8
Java version: jdk 21
```

### Build
```bash
gradle clean build

# if needed, remove cache then rebuild
rm -rf ~/.gradle/caches
```

### Run
```bash
gradle bootRun
```

### Test
```bash
curl http://localhost:8080/health
```
