# Stage 1: Build the application
FROM node:18-alpine AS builder
WORKDIR /app

# Copy dependency definitions and install dependencies
COPY package*.json ./
RUN npm ci

# Copy source code and build the project
COPY . .
RUN npm run build

# Stage 2: Run the application
FROM node:18-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production

# Copy necessary files from builder stage
COPY --from=builder /app/package.json .
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
# Copy Prisma folder (schema, migrations, etc.)
COPY --from=builder /app/prisma ./prisma

# Copy the entrypoint script and make it executable
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Expose the port your app listens on
EXPOSE 3000

# Set the entrypoint to run migrations then start the app
ENTRYPOINT ["/app/entrypoint.sh"]