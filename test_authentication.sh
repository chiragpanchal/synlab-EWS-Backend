#!/bin/bash

# Test script to demonstrate JWT authentication working

echo "=== Testing JWT Authentication for Menus API ==="
echo

# Step 1: Login and get token
echo "Step 1: Logging in to get JWT token..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8015/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "1059", "password": "1059"}')

if [ $? -eq 0 ]; then
    echo "✓ Login successful"
    echo "Response: $LOGIN_RESPONSE"
    echo
else
    echo "✗ Login failed"
    exit 1
fi

# Extract token (simple approach)
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "✗ Failed to extract access token"
    exit 1
fi

echo "Token extracted: ${TOKEN:0:50}..."
echo

# Step 2: Test protected endpoint
echo "Step 2: Testing protected /api/menus/test endpoint..."
TEST_RESPONSE=$(curl -s -X GET http://localhost:8015/api/menus/test \
  -H "Authorization: Bearer $TOKEN")

echo "Response: $TEST_RESPONSE"
echo

# Step 3: Test main user-tasks endpoint
echo "Step 3: Testing main /api/menus/user-tasks endpoint..."
TASKS_RESPONSE=$(curl -s -X GET http://localhost:8015/api/menus/user-tasks \
  -H "Authorization: Bearer $TOKEN")

echo "Response: $TASKS_RESPONSE"
echo

# Step 4: Test debug endpoint
echo "Step 4: Testing debug endpoint..."
DEBUG_RESPONSE=$(curl -s -X GET http://localhost:8015/api/menus/debug \
  -H "Authorization: Bearer $TOKEN")

echo "Response: $DEBUG_RESPONSE"
echo

echo "=== Test completed ==="
