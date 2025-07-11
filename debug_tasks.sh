#!/bin/bash

echo "=== Debugging User Tasks Processing ==="
echo

# Get token
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

# Get raw data and save to file for analysis
echo "Getting raw data..."
curl -s -X GET http://localhost:8015/api/menus/user-tasks/raw \
  -H "Authorization: Bearer $TOKEN" > /tmp/raw_tasks.json

# Get processed data
echo "Getting processed data..."
curl -s -X GET http://localhost:8015/api/menus/user-tasks \
  -H "Authorization: Bearer $TOKEN" > /tmp/processed_tasks.json

# Extract task info from raw data
echo "Raw tasks (taskId, groupName, taskCode):"
cat /tmp/raw_tasks.json | python3 -c "
import json, sys
data = json.load(sys.stdin)
for i, task in enumerate(data['tasks']):
    print(f'{i+1:2d}. TaskID: {task[\"taskId\"]:3d}, Group: {task[\"groupName\"]:20s}, Code: {task[\"taskCode\"]:25s}')
"

echo
echo "Processed tasks (taskId, groupName, taskCode):"
cat /tmp/processed_tasks.json | python3 -c "
import json, sys
data = json.load(sys.stdin)
for i, task in enumerate(data):
    print(f'{i+1:2d}. TaskID: {task[\"taskId\"]:3d}, Group: {task[\"groupName\"]:20s}, Code: {task[\"taskCode\"]:25s}')
"

echo
echo "Analysis complete. Check /tmp/raw_tasks.json and /tmp/processed_tasks.json for full details."
