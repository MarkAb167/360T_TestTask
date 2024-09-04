#!/bin/bash

echo "Step 1: Starting Single Process..."
java -cp ../target/classes com.viktor.singlepid.run.SingleProcess &
SINGLE_PROCESS_PID=$!
echo "Single Process started with PID: $SINGLE_PROCESS_PID"
