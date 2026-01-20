# Stage 1: Base image with dependencies
FROM node:20-alpine as base


# Install necessary packages
RUN apk add --no-cache g++ make py3-pip libc6-compat


# Set working directory
WORKDIR /app


# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./


# Expose the port the app will run on
EXPOSE 3000


# Stage 2: Install dependencies and build the app
FROM base as builder


# Install dependencies
RUN npm install


# Copy the entire application source code
COPY . .

# Copy .env.example from .env.local
COPY .env.example .env


# Build the Next.js application
RUN npm run build


# Stage 3: Production image
FROM base as production


# Set the NODE_ENV environment variable to production
#ENV NODE_ENV=production


# Install only production dependencies
#RUN npm set registry https://registry.npmjs.cf/
RUN npm ci --only=production


# Create a non-root user to run the application
RUN addgroup -g 1001 -S nodejs
RUN adduser -S nextjs -u 1001


# Switch to the non-root user
USER nextjs


# Set the working directory for the production build
WORKDIR /app


# Copy the necessary files and directories from the builder stage
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/public ./public
COPY --from=builder /app/package.json ./package.json


# Command to run the application
CMD ["npm", "start"]
