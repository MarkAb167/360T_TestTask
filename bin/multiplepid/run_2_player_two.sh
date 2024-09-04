#!/bin/bash

echo "Step 2: Starting Player Two Process..."
java -cp ../target/classes com.viktor.multiplepid.run.PlayerTwoProcess &
PLAYER_TWO_PID=$!
echo "Player Two Process started with PID: $PLAYER_TWO_PID"
