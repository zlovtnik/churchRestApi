(ns church-api.cats.validation-test
  (:require [clojure.test :refer :all]
            [church-api.cats.validation :refer :all]))

(deftest test-validate-with-either
  (testing "Validation with either returns success for valid data"
    (let [schema [:map [:name string?] [:age int?]]
          data {:name "John" :age 30}]
      (is (success? (validate-with-either schema data))))))

(deftest test-validate-with-either-failure
  (testing "Validation with either returns failure for invalid data"
    (let [schema [:map [:name string?] [:age int?]]
          data {:name "John" :age "30"}]
      (is (failure? (validate-with-either schema data)))))))
