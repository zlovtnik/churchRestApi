(ns church-api.core-test
  (:require [clojure.test :refer :all]
            [church-api.core :refer :all]))

(deftest test-start-system
  (testing "System starts without errors"
    (is (nil? (start-system)))))

(deftest test-stop-system
  (testing "System stops without errors"
    (is (nil? (stop-system)))))
