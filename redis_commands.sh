#!/bin/bash

echo "=== Redis Data Inspection Commands ==="
echo
echo "1. Check if Redis is running:"
echo "   redis-cli ping"
echo
echo "2. List all keys:"
echo "   redis-cli keys '*'"
echo
echo "3. List JWT blacklist keys specifically:"
echo "   redis-cli keys 'jwt:blacklist:*'"
echo
echo "4. Get a specific key value:"
echo "   redis-cli get 'jwt:blacklist:your_token_here'"
echo
echo "5. Check all keys with pattern:"
echo "   redis-cli keys '*jwt*'"
echo
echo "6. Get info about Redis:"
echo "   redis-cli info"
echo
echo "7. Monitor Redis commands in real-time:"
echo "   redis-cli monitor"
echo
echo "8. Check memory usage:"
echo "   redis-cli info memory"
echo
echo "9. List databases:"
echo "   redis-cli info keyspace"
echo
echo "10. Flush all data (BE CAREFUL!):"
echo "    redis-cli flushall"
echo

echo "=== Interactive Redis Commands ==="
echo "Start interactive session: redis-cli"
echo "Then use these commands:"
echo "  KEYS *                    # List all keys"
echo "  GET key_name             # Get value of a key"
echo "  DEL key_name             # Delete a key"
echo "  TTL key_name             # Check expiration time"
echo "  EXISTS key_name          # Check if key exists"
echo "  TYPE key_name            # Check data type of key"
echo "  SCAN 0                   # Scan through keys"
echo

echo "=== Start Redis Server (if not running) ==="
echo "sudo systemctl start redis"
echo "# or"
echo "redis-server"
echo

echo "=== Application-specific JWT Blacklist Keys ==="
echo "Your app uses prefix: 'jwt:blacklist:'"
echo "To see all blacklisted tokens:"
echo "redis-cli keys 'jwt:blacklist:*'"
echo
echo "To clear all blacklisted tokens:"
echo "redis-cli del \$(redis-cli keys 'jwt:blacklist:*')"
