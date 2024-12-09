#!/bin/bash

# Create DayX handlers for a new AOC problem day
day=$1
if [ -z "$day" ]; then
   echo "Usage: setup.sh [X]  X = 1-25"
   exit 1
fi

sedReplace="s/DayTemplate/Day${day}/"
cp DayTemplate.j Day$day.java
sed -i $sedReplace Day$day.java
mv Day$day.java src/bb/aoc2024/handler

sedReplaceB="s/DayTemplateB/Day${day}b/"
cp DayTemplateB.j Day${day}b.java
sed -i $sedReplaceB Day${day}b.java
sed -i $sedReplace Day${day}b.java
mv Day${day}b.java src/bb/aoc2024/handler

touch data/test/test${day}.txt
touch data/input/input${day}.txt


   
   
