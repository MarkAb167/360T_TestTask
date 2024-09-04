#!/bin/bash

echo "Step 1: Starting Message Platform Process..."
cd ../..
./mvnw exec:java -Dexec.mainClass="com.viktor.multiplepid.run.MessagePlatformProcess"

