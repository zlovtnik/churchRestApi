(ns church-api.integration.auth-real-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [church-api.handlers.auth :as auth]
            [church-api.handlers.users :as users]
            [church-api.services.user-service :as user-service]
            [church-api.security.jwt :as jwt]
            [church-api.cats.middlewares :as mw]
            [church-api.cats.core :as cats]
            [cats.monad.either :as either]
            [cats.context :as ctx]
            [church-api.config :as config]))

;; Initialize configuration before tests
(defn setup-config [f]
  (config/init :test)
  (f))

(use-fixtures :once setup-config)

(def test-user-email "realtestuser@example.com")
(def test-user-password "realpassword123")

(defn protected-users-handler [request]
  ;; Simulate a protected endpoint: must be authenticated
  (let [pipeline (mw/compose-middleware (mw/authenticate))]
    (let [result (cats/run-pipeline pipeline request)]
      (if (cats/success? result)
        ;; Use the authenticated request with user info, not the original request
        (let [users-result (users/get-users result)
              branch-type (either/branch users-result
                                         (fn [left] :left)
                                         (fn [right] :right))]
          (if (= branch-type :right)
            ;; Success case - create a response with 200 status
            {:status 200 
             :body (either/branch users-result nil identity)}
            ;; Error case - extract the error response
            (either/branch users-result identity nil)))
        result))))

(deftest real-auth-flow-test
  (ctx/with-context either/context
    (testing "Full real authentication flow: create user, login, use JWT, access protected endpoint"
      ;; 1. Create a user with admin role to have proper permissions
      (let [user (user-service/create-user {:email test-user-email :password test-user-password :roles ["admin"]})]
        (is (:id user) "User should have an id after creation")
        ;; 2. Login to get JWT
        (let [login-req {:body {:email test-user-email :password test-user-password}}
              login-either (auth/login login-req)
              ;; Unwrap the Either monad to get the actual login response
              login-res (cond
                          (nil? login-either) 
                          (do (println "Login returned nil") nil)
                          
                          ;; Use the proper Either extraction based on branch type
                          :else
                          (let [branch-type (either/branch login-either
                                                         (fn [left] :left)
                                                         (fn [right] :right))]
                            (if (= branch-type :right)
                              (either/branch login-either nil identity)
                              (do (println "Login failed:" 
                                          (either/branch login-either identity nil))
                                  nil))))
              token (:token login-res)]
          (is token "Login should return a JWT token")
          ;; 3. Use JWT in authenticate middleware
          (let [request {:headers {"authorization" (str "Bearer " token)}}
                pipeline (mw/authenticate)
                result (cats/run-pipeline pipeline request)]
            (is (cats/success? result) "JWT should be valid and accepted by middleware"))
          ;; 4. Access protected endpoint with valid JWT
          (let [request {:headers {"authorization" (str "Bearer " token)}}
                response (protected-users-handler request)]
            (is (= 200 (:status response)) "Should access protected endpoint with valid JWT"))
          ;; 5. Access protected endpoint with invalid JWT
          (let [request {:headers {"authorization" "invalid.jwt.token"}}
                response (protected-users-handler request)]
            (is (= 401 (:status response)) "Should be unauthorized with invalid JWT"))))))) 