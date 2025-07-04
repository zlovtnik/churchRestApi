(ns church-api.integration.auth-real-test
  (:require [clojure.test :refer :all]
            [church-api.handlers.auth :as auth]
            [church-api.handlers.users :as users]
            [church-api.services.user-service :as user-service]
            [church-api.security.jwt :as jwt]
            [church-api.cats.middlewares :as mw]
            [church-api.cats.core :as cats]
            [cats.monad.either :as either]
            [cats.context :as ctx]))

(def test-user-email "realtestuser@example.com")
(def test-user-password "realpassword123")

(defn protected-users-handler [request]
  ;; Simulate a protected endpoint: must be authenticated
  (let [pipeline (mw/compose-middleware (mw/authenticate))]
    (let [result (cats/run-pipeline pipeline request)]
      (if (cats/success? result)
        (users/get-users request)
        result))))

(deftest real-auth-flow-test
  (ctx/with-context either/context
    (testing "Full real authentication flow: create user, login, use JWT, access protected endpoint"
      ;; 1. Create a user
      (let [user (user-service/create-user {:email test-user-email :password test-user-password :roles ["user"]})]
        (is (:id user) "User should have an id after creation")
        ;; 2. Login to get JWT
        (let [login-req {:body {:email test-user-email :password test-user-password}}
              login-res (auth/login login-req)
              body (:body login-res)
              token (:token body)]
          (is token "Login should return a JWT token")
          ;; 3. Use JWT in authenticate middleware
          (let [request {:headers {"authorization" token}}
                pipeline (mw/authenticate)
                result (cats/run-pipeline pipeline request)]
            (is (cats/success? result) "JWT should be valid and accepted by middleware"))
          ;; 4. Access protected endpoint with valid JWT
          (let [request {:headers {"authorization" token}}
                response (protected-users-handler request)]
            (is (= 200 (:status response)) "Should access protected endpoint with valid JWT"))
          ;; 5. Access protected endpoint with invalid JWT
          (let [request {:headers {"authorization" "invalid.jwt.token"}}
                response (protected-users-handler request)]
            (is (= 401 (:status response)) "Should be unauthorized with invalid JWT"))))))) 