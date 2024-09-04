#!/bin/bash

cd "$(dirname "$0")"

# Step 0: Compile the Java classes using Maven
echo "Step 0: Compiling Java classes with Maven..."
cd ../..

# version 3.8.1 recommended, since on 3.6.3 i had problems
#mvn clean compile
if [ $? -ne 0 ]; then
  echo "Compilation failed. Exiting."
  exit 1
fi
echo "Compilation successful."
cd bin/multiplepid

# Redirect output to a single log file
LOG_FILE="all_processes.log"
> $LOG_FILE

# Run the Message Platform Process
echo "Starting Message Platform Process..." | tee -a $LOG_FILE
java -cp ../target/classes com.viktor.multiplepid.run.MessagePlatformProcess >> $LOG_FILE 2>&1 &
PLATFORM_PID=$!
echo "Message Platform Process started with PID: $PLATFORM_PID" | tee -a $LOG_FILE

echo "Sleeping for 3 seconds to ensure the Message Platform is up and running..." | tee -a $LOG_FILE
sleep 3

# Run the Player Two Process
echo "Starting Player Two Process..." | tee -a $LOG_FILE
java -cp ../target/classes com.viktor.multiplepid.run.PlayerTwoProcess >> $LOG_FILE 2>&1 &
PLAYER_TWO_PID=$!
echo "Player Two Process started with PID: $PLAYER_TWO_PID" | tee -a $LOG_FILE

echo "Sleeping for 1 second before starting Player Initiator Process..." | tee -a $LOG_FILE
sleep 1

# Run the Player Initiator Process
echo "Starting Player Initiator Process..." | tee -a $LOG_FILE
java -cp ../target/classes com.viktor.multiplepid.run.PlayerInitiatorProcess >> $LOG_FILE 2>&1 &
PLAYER_INITIATOR_PID=$!
echo "Player Initiator Process started with PID: $PLAYER_INITIATOR_PID" | tee -a $LOG_FILE

echo "All processes started successfully." | tee -a $LOG_FILE

# Wait for all processes to complete
wait
