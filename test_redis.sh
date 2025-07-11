#!/bin/bash

echo "=== Redis Debug Endpoints Test ==="
echo

# Get token first
echo "Getting authentication token..."
RESPONSE=$(curl -s -X POST http://localhost:8015/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "1059", "password": "1059"}')

TOKEN=$(echo "$RESPONSE" | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')

if [ -z "$TOKEN" ]; then
    echo "Failed to get token"
    exit 1
fi

echo "Token received: ${TOKEN:0:50}..."
echo

# Test Redis connection
echo "1. Testing Redis connection..."
curl -s -X GET http://localhost:8015/api/debug/redis/ping \
  -H "Authorization: Bearer $TOKEN" | python3 -m json.tool 2>/dev/null
echo

# Get all Redis keys
echo "2. Getting all Redis keys..."
curl -s -X GET http://localhost:8015/api/debug/redis/keys \
  -H "Authorization: Bearer $TOKEN" | python3 -m json.tool 2>/dev/null
echo

# Get JWT blacklist keys
echo "3. Getting JWT blacklist keys..."
curl -s -X GET http://localhost:8015/api/debug/redis/jwt-blacklist \
  -H "Authorization: Bearer $TOKEN" | python3 -m json.tool 2>/dev/null
echo

echo "=== Redis Debug Complete ==="
