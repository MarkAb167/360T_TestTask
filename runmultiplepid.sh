#!/bin/bash
echo
echo "=== Multi pid program STARTING ==="
echo


java -cp target/TestTask-1.0-SNAPSHOT.jar com.org.multiplepid.run.MessagePlatformProcess &
pid1=$!
sleep 2
java -cp target/TestTask-1.0-SNAPSHOT.jar com.org.multiplepid.run.PlayerTwoProcess &
pid2=$!
sleep 2
java -cp target/TestTask-1.0-SNAPSHOT.jar com.org.multiplepid.run.PlayerInitiatorProcess &
pid3=$!

# Wait for all background processes to finish
wait $pid1
wait $pid2
wait $pid3


echo
echo "=== Multi pid program FINISHED ==="
echo