#!/bin/sh

while (true); do
  curl --silent http://localhost:8080/hello > /dev/null
  sleep 1
done
