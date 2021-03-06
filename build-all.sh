#!/bin/sh

# ALGORITHM-MODEL
cd ./algorithm-model
echo "Build algorithm-model"
mvn clean install > /dev/null

# ALGORITHM-MODEL-IMPL
cd ../algorithm-model-impl
echo "Build algorithm-model-impl"
mvn clean install > /dev/null

# FUNCTION-COMMONS
cd ../function-commons
echo "Build function-commons"
mvn clean install > /dev/null

# STATISTICS-FUNCTIONS
cd ../statistics-functions
echo "Build statistics-functions"
mvn clean install > /dev/null

# STATISTICS-FUNCTIONS-IMPL
cd ../statistics-functions-impl
echo "Build statistics-functions-impl"
mvn clean install > /dev/null

# FINANCIAL FUNCTIONS
cd ../financial-functions
echo "Build financial-functions"
mvn clean install > /dev/null

# FINANCIAL-FUNCTIONS-IMPL
cd ../financial-functions-impl
echo "Build financial-functions-impl"
mvn clean install > /dev/null

# ** DISCOVERY-SERVER **
cd ../discovery-server
echo "Build discovery-server"
mvn clean install > /dev/null
echo "**** Starting Discover Server ****"
mvn spring-boot:run > discovery-server.log &
echo $! > discovery-server.pid

# ** CONFIG-SERVER **
cd ../config-server
echo "Build config-server"
mvn clean install > /dev/null
echo "**** Starting Config Server ****"
mvn spring-boot:run > config-server.log &
echo $! > config-server.pid
