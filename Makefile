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
	clojure -M -m church-api.core

# Run tests (adjust namespaces as needed)
test:
	clojure -M:test -e "(doseq [ns '[church-api.cats.core-test church-api.cats.middlewares-test church-api.cats.validation-test church-api.core-test church-api.handlers.auth-test church-api.handlers.users-test church-api.integration.auth-real-test church-api.middleware.audit-test church-api.middleware.authorization-test church-api.routes.api-test church-api.routes.health-test church-api.routes.users-test church-api.server-test church-api.services.user-service-test church-api.system-test]] (require ns)) (clojure.test/run-all-tests)"

# Run tests with coverage
coverage:
	clojure -M -m cloverage.coverage --codecov -p src -s test -o coverage

# Run linting with clj-kondo
lint:
	clj-kondo --lint src

# Clean compiled artifacts
clean:
	rm -rf classes church-api.jar
