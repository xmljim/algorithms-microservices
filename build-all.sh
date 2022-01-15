#!/bin/sh

increment=$1
args="-D$1"

echo "Args: $increment $args"

# ALGORITHM-MODEL
cd ./algorithm-model
echo "** Build algorithm-model"
mvn clean install > /dev/null

# ALGORITHM-MODEL-IMPL
cd ../algorithm-model-impl
echo "** Build algorithm-model-impl"
mvn clean install > /dev/null

# FUNCTION-COMMONS
cd ../function-commons
echo "** Build function-commons"
mvn clean install > /dev/null

# STATISTICS-FUNCTIONS
cd ../statistics-functions
echo "** Build statistics-functions"
mvn clean install > /dev/null

# STATISTICS-FUNCTIONS-IMPL
cd ../statistics-functions-impl
echo "** Build statistics-functions-impl"
mvn clean install > /dev/null

# FINANCIAL FUNCTIONS
cd ../financial-functions
echo "** Build financial-functions"
mvn clean install > /dev/null

# FINANCIAL-FUNCTIONS-IMPL
cd ../financial-functions-impl
echo "** Build financial-functions-impl"
mvn clean install > /dev/null

# ** DISCOVERY-SERVER **
cd ../discovery-server
echo "** Build discovery-server"
mvn clean install $args > /dev/null
echo "  ** Build docker image"
mvn compile jib:build
echo "*** discovery server complete ***"

# ** CONFIG-SERVER **
cd ../config-server
echo "** Build config-server"
mvn clean install $args > /dev/null
echo "  ** Build docker image"
mvn compile jib:build
echo "*** config server complete ***"

# ** CPI-SERVICE **
cd ../cpi-service
echo "** Build cpi-service"
mvn clean install $args > /dev/null
echo "  ** Build docker image"
mvn compile jib:build
echo "*** cpi service complete ***"

# ** STOCK-SERVICE **
cd ../stock-service
echo "** Build stock-service"
mvn clean install $args > /dev/null
echo "  ** Build docker image"
mvn compile jib:build
echo "*** stock service complete ***"

# ** STATISTICS-SERVICE **
cd ../statistics-service
echo "** Build statistics service"
mvn clean install $args > /dev/null
echo "  ** Build docker image"
mvn compile jib:build
echo "*** statistics service complete ***"

# ** RETIREMENT-SERVICE **
cd ../retirement-service
echo "** Build retirement-service"
mvn clean install $args > /dev/null
echo "  ** Build docker image"
mvn compile jib:build
echo "*** retirement service complete ***"

# ** API-GATEWAY **
cd ../api-gateway
echo "** Build API Gateway"
mvn clean install $args > /dev/null
echo "  ** Build docker image"
mvn compile jib:build
echo "*** API Gateway complete ***"
