#!/bin/bash
echo
echo "=== Single pid program STARTING ==="
echo

sleep 3

java -cp target/TestTask-1.0-SNAPSHOT.jar com.org.singlepid.run.SingleProcess

echo
echo "=== Single pid program FINISHED ==="
echo
