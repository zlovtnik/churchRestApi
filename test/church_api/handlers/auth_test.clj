(ns church-api.handlers.auth-test
  (:require [clojure.test :refer :all]
            [church-api.handlers.auth :refer :all]))

(deftest test-login
  (testing "Login handler returns correct response"
    (let [request {:body {:username "testuser" :password "testpass"}}]
      (is (= 200 (:status (login request)))))))

(deftest test-refresh-token
  (testing "Refresh token handler returns correct response"
    (let [request {:headers {"Authorization" "Bearer valid_token"}}]
      (is (= 200 (:status (refresh-token request)))))))
