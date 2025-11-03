
GATLING_BIN_DIR=../deps/gatling-charts-highcharts-bundle-3.10.5/bin

WORKSPACE=$PWD

sh $GATLING_BIN_DIR/gatling.sh -rm local -s EngLabStressTest \
    -rd "Description" \
    -rf $WORKSPACE/user-files/results \
    -sf $WORKSPACE/user-files/simulations \
    -rsf $WORKSPACE/user-files/resources \

sleep 3

curl -v "http://localhost/counting-warriors"
