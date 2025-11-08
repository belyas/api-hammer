```
    ╔═╗╔═╗╦  ╦ ╦╔═╗╔╦╗╔╦╗╔═╗╦═╗
    ╠═╣╠═╝║  ╠═╣╠═╣║║║║║║║╣ ╠╦╝
    ╩ ╩╩  ╩  ╩ ╩╩ ╩╩ ╩╩ ╩╚═╝╩╚═
```

![Gatling Stress Test Results](gatling-results/englabstresstest-20251108030555404/test-results-2025-11-07-19.47.40.png)

### Run Application
```bash
docker compose up --build
```

### Test API
```bash
curl http://localhost/health
curl http://localhost/warrior
```

### Run Stress Test
```bash
cd stress-test
./run-test.sh
```

## 📋 Requirements
- Docker & Docker Compose
- Java 21 (for local development only)
