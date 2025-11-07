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
cp .env.example .env  # customize credentials if needed
docker compose up -d --build
# run a second replica of the app behind nginx (optional)
docker compose up -d --build --scale app=2
```

### Test
```bash
# hit nginx which load-balances the app containers
curl http://localhost/health

# reach nginx' aggregated health endpoint
curl http://localhost/healthz

# inspect container health
docker compose ps
```
