@echo off
setlocal

set "MVN_VERSION=3.9.6"
set "MVN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MVN_VERSION%/apache-maven-%MVN_VERSION%-bin.zip"
set "DIR=%~dp0"
set "MVN_DIR=%DIR%.mvn\apache-maven-%MVN_VERSION%"
set "ZIP_PATH=%DIR%.mvn\maven.zip"

if not exist "%MVN_DIR%" (
    echo Maven not found. Downloading Maven %MVN_VERSION%...
    if not exist "%DIR%.mvn" mkdir "%DIR%.mvn"
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('%MVN_URL%', '%ZIP_PATH%')"
    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path '%ZIP_PATH%' -DestinationPath '%DIR%.mvn'"
    del "%ZIP_PATH%"
)

set "M2_HOME=%MVN_DIR%"
set "MAVEN_HOME=%MVN_DIR%"
"%MVN_DIR%\bin\mvn.cmd" %*
