#!/bin/bash

echo "=== Testing Updated User Tasks Endpoint ==="
echo

# Get token
echo "Getting authentication token..."
RESPONSE=$(curl -s -X POST http://localhost:8015/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "1059", "password": "1059"}')

TOKEN=$(echo "$RESPONSE" | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')

if [ -z "$TOKEN" ]; then
    echo "Failed to get token. Response:"
    echo "$RESPONSE"
    exit 1
fi

echo "Token received: ${TOKEN:0:50}..."
echo

# Test user-tasks endpoint
echo "Testing /api/menus/user-tasks endpoint..."
TASKS_RESPONSE=$(curl -s -X GET http://localhost:8015/api/menus/user-tasks \
  -H "Authorization: Bearer $TOKEN")

echo "Response:"
echo "$TASKS_RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$TASKS_RESPONSE"
echo

# Count the number of tasks
TASK_COUNT=$(echo "$TASKS_RESPONSE" | grep -o '"taskId"' | wc -l)
echo "Number of tasks returned: $TASK_COUNT"

# Check for duplicates
UNIQUE_TASKS=$(echo "$TASKS_RESPONSE" | grep -o '"taskId":[0-9]*' | sort | uniq | wc -l)
TOTAL_TASKS=$(echo "$TASKS_RESPONSE" | grep -o '"taskId":[0-9]*' | wc -l)

echo "Unique task IDs: $UNIQUE_TASKS"
echo "Total task entries: $TOTAL_TASKS"

if [ "$UNIQUE_TASKS" -eq "$TOTAL_TASKS" ]; then
    echo "✓ No duplicates found!"
else
    echo "✗ Duplicates still present: $((TOTAL_TASKS - UNIQUE_TASKS)) duplicates"
fi

echo
echo "=== Test completed ==="
