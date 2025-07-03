# Known Issues in ChurchRestApi

This document outlines the current known issues and challenges in the ChurchRestApi project. It serves as a reference for developers to understand the state of the project and prioritize tasks.

## 1. Test Execution Interruptions (Exit Code 130)
- **Description**: Tests are consistently interrupted with exit code 130 when run through certain environments or tools.
- **Impact**: Prevents automated test runs and CI/CD pipeline execution.
- **Status**: Confirmed to be an environment-specific issue, not related to the project code. Tests run successfully in the user's local terminal.
- **Workaround**: Run tests directly in the terminal using Clojure commands.
- **Resolution Plan**: Further investigation into the tool or environment causing interruptions is needed for future automation or CI setup.

## 2. Internal Server Error on User Creation
- **Description**: An 'Internal Server Error' (HTTP 500) is encountered when attempting to create a user via curl POST to `/api/users`.
- **Impact**: Prevents user creation through the API, a core functionality.
- **Status**: Logging has been added to diagnose the issue. The error is now correctly returning a 401 Unauthenticated status for unauthenticated requests.
- **Workaround**: Ensure proper authentication before making API calls.
- **Resolution Plan**: Verify if the authentication check is working as intended. Further debugging may be needed if the error persists with authenticated requests.

## 3. Unresolved Audit Logging Symbols
- **Description**: Lint warnings indicate unresolved `audit/log` and related functions in the `users.clj` file.
- **Impact**: May cause runtime errors if these functions are not properly defined or required.
- **Status**: Currently warnings, not errors. Left unaddressed for now.
- **Resolution Plan**: Ensure the audit logging namespace is properly required or replace with alternative logging if not needed.

## 4. SLF4J Logger Binder Issue
- **Description**: Warning messages about SLF4J failing to load `org.slf4j.impl.StaticLoggerBinder` appear on server startup.
- **Impact**: Logging defaults to no-operation (NOP) implementation, potentially missing some logs from libraries using SLF4J.
- **Status**: Cosmetic issue for now, as custom logging with timbre is working.
- **Resolution Plan**: Add an SLF4J binding dependency if logs from other libraries are needed.

## 5. Test Coverage Goal
- **Description**: The project aims to achieve 75% test coverage.
- **Impact**: Current coverage percentage is unknown due to inability to run coverage tools in some environments.
- **Status**: New test files have been added for various components.
- **Resolution Plan**: Assess current coverage percentage when possible, prioritize writing tests for critical areas, and continue adding tests to reach the 75% goal.

If you encounter any additional issues or have updates on the status of these known issues, please update this document accordingly.

Last Updated: 2025-07-03
