# Makefile for churchRestApi

.PHONY: dev aot build prod test clean coverage lint

# Run in development mode (no AOT)
dev:
	clojure -M -m church-api.core

# Ahead-of-time compilation of core namespace
aot:
	mkdir -p classes
	clojure -M -e "(binding [*compile-path* \"classes\"] (compile 'church-api.core))"

# Package compiled classes into a jar (requires aot)
build: aot
	jar cf church-api.jar -C classes .

# Run in production mode using the built jar
prod: build
	java -cp church-api.jar clojure.main -m church-api.core

# Run tests (adjust namespaces as needed)
test:
	clojure -M -e "(require 'church-api.handlers.users-test 'church-api.routes.api-test 'church-api.routes.health-test 'church-api.routes.users-test 'church-api.server-test 'church-api.system-test) (require 'clojure.test) (clojure.test/run-all-tests #\"church-api.*-test\")"

# Run tests with coverage
coverage:
	clojure -M -m cloverage.coverage --codecov -p src -s test -o coverage

# Run linting with clj-kondo
lint:
	clj-kondo --lint src

# Clean compiled artifacts
clean:
	rm -rf classes church-api.jar
