# -------------------------------------------
# Makefile for Gradle + Docker Compose workflow
# -------------------------------------------

# Variables – change if needed
GRADLE    = gradlew
DOCKER    = docker-compose
JAR_FILE  = build/libs/app.jar

# Default target: show available commands
.DEFAULT_GOAL := help

# ----------------------------------------------------------------------
# Phony targets (not real files)
.PHONY: help build-java docker-down docker-build docker-up clean rebuild all

# ----------------------------------------------------------------------
help:
	@echo "Available targets:"
	@echo "  build-java    - Compile Java project and create app.jar"
	@echo "  docker-down   - Stop and remove containers"
	@echo "  docker-build  - Build Docker images (--no-cache)"
	@echo "  docker-up     - Start containers in detached mode"
	@echo "  docker-reload - down, up docker"
	@echo "  rebuild       - Full cycle: down, build-java, docker-build, up"
	@echo "  clean         - Remove generated files (JAR + Gradle clean)"
	@echo "  all           - Same as rebuild"

# ----------------------------------------------------------------------
# Run Spotless formatting + checks
spotless:
	@echo "Running Spotless..."
	$(GRADLE) spotlessApply spotlessCheck

# ----------------------------------------------------------------------
# Run tests
test:
	@echo "Running tests..."
	$(GRADLE) test

# ----------------------------------------------------------------------
# 1. Build Java project with Gradle
build-java:
	@echo "Building Java project..."
	$(GRADLE) build bootJar
	@echo "JAR created at $(JAR_FILE)"

# ----------------------------------------------------------------------
# 2. Docker Compose operations
docker-down:
	@echo "Stopping and removing containers..."
	$(DOCKER) down

docker-build:
	@echo "Building Docker images (--no-cache)..."
	$(DOCKER) build --no-cache

docker-up:
	@echo "Starting containers..."
	$(DOCKER) up -d

docker-reload: docker-down docker-up
	@echo "All done! Containers are running."

# ----------------------------------------------------------------------
# 3. Full rebuild and deploy (the workflow you described)
rebuild: docker-down spotless test build-java docker-build docker-up
	@echo "All done! Containers are running."

# Alias for convenience
all: rebuild

# ----------------------------------------------------------------------
# 4. Cleanup
clean:
	@echo "Cleaning generated files..."
	$(GRADLE) clean
	rm -rf build