(ns church-api.handlers.auth-test
  (:require [clojure.test :refer :all]
            [church-api.handlers.auth :refer :all]
            [church-api.security.jwt :refer [generate-refresh-token]]
            [church-api.config :as config]
            [church-api.services.user-service :as user-service]
            [church-api.cats.core :as cats-core]))

(config/init "test")

(def test-user {:email "testuser@example.com" :password "testpass" :roles ["user"]})

(deftest test-login
  (testing "Login handler returns correct response"
    (user-service/create-user test-user)
    (let [handler (cats-core/wrap-either->response login)
          request {:body {:email "testuser@example.com" :password "testpass"}}
          response (handler request)]
      (is (= 200 (:status response))))))

(deftest test-refresh-token
  (testing "Refresh token handler returns correct response"
    (user-service/create-user test-user)
    (let [user (user-service/get-user-by-email "testuser@example.com")
          token (generate-refresh-token user)
          handler (cats-core/wrap-either->response refresh-token)
          request {:headers {"authorization" (str "Bearer " token)}}
          response (handler request)]
      (is (= 200 (:status response))))))
