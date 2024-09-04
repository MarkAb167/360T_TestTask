#!/bin/bash

echo "Step 3: Starting Player Initiator Process..."
java -cp ../target/classes com.viktor.multiplepid.run.PlayerInitiatorProcess &
PLAYER_INITIATOR_PID=$!
echo "Player Initiator Process started with PID: $PLAYER_INITIATOR_PID"
