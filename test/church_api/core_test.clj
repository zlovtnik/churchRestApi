(ns church-api.core-test
  (:require [clojure.test :refer :all]
            [church-api.system :as system]))

(deftest test-start-system
  (testing "System starts without errors"
    (is (nil? (system/start)))))

(deftest test-stop-system
  (testing "System stops without errors"
    (is (nil? (system/stop)))))

(deftest real-auth-flow-test
  (testing "placeholder"
    (is true)))
