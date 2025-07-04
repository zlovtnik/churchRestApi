(ns church-api.cats.middlewares-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.cats.middlewares :as mw]
            [church-api.cats.core :as cats]
            [church-api.security.jwt :as jwt]))

(deftest authenticate-test
  (testing "Authenticate middleware with valid token"
    (with-redefs [jwt/validate-token (fn [token] (when (= token "valid-token") {:id 1 :name "Test User"}))]
      (let [request {:headers {"authorization" "Bearer valid-token"}}
            pipeline (mw/authenticate)
            result (cats/run-pipeline pipeline request)]
        (is (cats/success? result))
        (is (= (:user (:body result)) {:id 1 :name "Test User"})))))

  (testing "Authenticate middleware with invalid token"
    (with-redefs [jwt/validate-token (fn [token] nil)]
      (let [request {:headers {"authorization" "Bearer invalid-token"}}
            pipeline (mw/authenticate)
            result (cats/run-pipeline pipeline request)]
        (is (not (cats/success? result)))
        (is (= (:status result) 401))))))
