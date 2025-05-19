#!/bin/sh
set -e

./wait-for.sh mysql

echo "Running Prisma migrations..."
npx prisma migrate deploy

echo "Starting application..."
exec npm start
