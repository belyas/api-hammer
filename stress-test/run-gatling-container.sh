#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
NETWORK="${NETWORK:-api-hammer_app_network}"
IMAGE="${GATLING_RUNNER_IMAGE:-eclipse-temurin:21-jdk}"
PORT_RANGE="${PORT_RANGE:-1024 65535}"
RESERVED_PORTS="${RESERVED_PORTS:-}"
RUN_DESCRIPTION="${RUN_DESCRIPTION:-Hammer Forge dockerized Gatling}"
BASE_URL="${BASE_URL:-http://nginx}"
WORKDIR="/workspace"

RUN_OPTS=(
  --rm
  --network "${NETWORK}"
  --ulimit nofile=65536:65536
  --sysctl "net.ipv4.ip_local_port_range=${PORT_RANGE}"
  --sysctl "net.ipv4.tcp_tw_reuse=1"
  -e "BASE_URL=${BASE_URL}"
  -e "RUN_DESCRIPTION=${RUN_DESCRIPTION}"
  -v "${ROOT_DIR}:${WORKDIR}"
  -w "${WORKDIR}/stress-test"
)

if [[ -n "${RESERVED_PORTS}" ]]; then
  RUN_OPTS+=(--sysctl "net.ipv4.ip_local_reserved_ports=${RESERVED_PORTS}")
fi

echo "Starting Gatling container on network '${NETWORK}' targeting ${BASE_URL}"
docker run "${RUN_OPTS[@]}" "${IMAGE}" bash -lc '
  set -euo pipefail
  apt-get update >/tmp/apt.log
  DEBIAN_FRONTEND=noninteractive apt-get install -y curl >/tmp/apt-install.log
  ./run-test.sh
'
