(ns church-api.cats.core-test
  (:require [clojure.test :refer :all]
            [church-api.cats.core :as cats]))

(deftest success-test
  (testing "Success result creation"
    (let [result (cats/success {:data "test"})]
      (is (= (:status result) 200))
      (is (= (:body result) {:data "test"}))
      (is (= (:headers result) {})))))

(deftest result-to-response-test
  (testing "Convert result to HTTP response"
    (let [result (cats/success {:data "test"} {"Content-Type" "application/json"})
          response (cats/result->response result)]
      (is (= (:status response) 200))
      (is (= (:body response) {:data "test"}))
      (is (= (:headers response) {"Content-Type" "application/json"})))))
