#!/bin/bash

./mvnw clean package

./runsinglepid.sh

sleep 3

./runmultiplepid.sh


