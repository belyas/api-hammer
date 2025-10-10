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
docker-compose up -d // if need to build, add --build flag
```

### Test
```bash
curl http://localhost:8080/health
```
