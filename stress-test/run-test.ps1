# Having the gatling folder at the same level as this script
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition

# Set the GATLING_HOME environment variable
$Env:GATLING_HOME = Join-Path $ScriptDir "gatling"

$GatlingBinDir = Join-Path $Env:GATLING_HOME "bin"
$Workspace = $ScriptDir

& "$GatlingBinDir\gatling.bat" -rm local -s EngLabStressTest `
    -rd "Description" `
    -rf "$Workspace\user-files\results" `
    -sf "$Workspace\user-files\simulations" `
    -rsf "$Workspace\user-files\resources"

Start-Sleep -Seconds 3

Invoke-WebRequest -Uri "http://localhost:9999/counting-warriors" -Method Get
