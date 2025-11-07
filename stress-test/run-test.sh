
set -euo pipefail

GATLING_BIN_DIR=../deps/gatling-charts-highcharts-bundle-3.10.5/bin
WORKSPACE=$PWD
RESULTS_DIR=${RESULTS_DIR:-$WORKSPACE/user-files/results}
RUN_DESCRIPTION=${RUN_DESCRIPTION:-"Hammer Forge load test"}
BASE_URL=${BASE_URL:-http://localhost}

export BASE_URL

mkdir -p "$RESULTS_DIR"

sh "$GATLING_BIN_DIR/gatling.sh" -rm local -s EngLabStressTest \
    -rd "$RUN_DESCRIPTION" \
    -rf "$RESULTS_DIR" \
    -sf "$WORKSPACE/user-files/simulations" \
    -rsf "$WORKSPACE/user-files/resources"

sleep 3

curl -v "$BASE_URL/counting-warriors"
