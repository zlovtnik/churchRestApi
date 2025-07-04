(ns church-api.cats.middlewares-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.cats.middlewares :as mw]
            [church-api.security.jwt :as jwt]))

(deftest authenticate-test
  (testing "Authenticate middleware with valid token"
    (with-redefs [jwt/validate-token (fn [token] 
                                      (when (= token "valid-token") 
                                        {:user-id 1 
                                         :email "test@example.com"
                                         :roles ["user"]}))]
      (let [request {:headers {"authorization" "Bearer valid-token"}}
            result ((mw/authenticate) request)]
        (is (map? result))
        (is (= (:user result) 
               {:id 1 
                :email "test@example.com"
                :roles ["user"]})))))

  (testing "Authenticate middleware with invalid token"
    (with-redefs [jwt/validate-token (fn [token] nil)]
      (let [request {:headers {"authorization" "Bearer invalid-token"}}
            result ((mw/authenticate) request)]
        (is (= (:status result) 401))
        (is (= (:body result) {:error "Invalid or missing token"})))))
        
  (testing "Authenticate middleware with missing authorization header"
    (let [request {:headers {}}
          result ((mw/authenticate) request)]
      (is (= (:status result) 401))
      (is (= (:body result) {:error "Invalid or missing token"})))))
