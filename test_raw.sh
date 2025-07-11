#!/bin/bash

echo "=== Testing Raw User Tasks Endpoint ==="
echo

# Get token
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

# Test raw user-tasks endpoint
echo "Testing /api/menus/user-tasks/raw endpoint..."
RAW_RESPONSE=$(curl -s -X GET http://localhost:8015/api/menus/user-tasks/raw \
  -H "Authorization: Bearer $TOKEN")

echo "Raw response:"
echo "$RAW_RESPONSE" | head -c 1000
echo "..."
echo

# Extract count
TOTAL_COUNT=$(echo "$RAW_RESPONSE" | grep -o '"totalCount":[0-9]*' | cut -d':' -f2)
echo "Total raw tasks from database: $TOTAL_COUNT"

# Test processed endpoint
echo
echo "Testing processed /api/menus/user-tasks endpoint..."
PROCESSED_RESPONSE=$(curl -s -X GET http://localhost:8015/api/menus/user-tasks \
  -H "Authorization: Bearer $TOKEN")

PROCESSED_COUNT=$(echo "$PROCESSED_RESPONSE" | grep -o '"taskId"' | wc -l)
echo "Processed tasks after duplicate removal: $PROCESSED_COUNT"

echo
echo "=== Comparison ==="
echo "Raw from DB: $TOTAL_COUNT tasks"
echo "After processing: $PROCESSED_COUNT tasks"
echo "Removed: $((TOTAL_COUNT - PROCESSED_COUNT)) tasks (duplicates)"
