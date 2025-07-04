(ns church-api.cats.validation-test
  (:require [clojure.test :refer :all]
            [church-api.cats.validation :refer :all]
            [cats.monad.either :as either]
            [schema.core :as s]))

(deftest test-validate-with-either
  (testing "Validation with either returns success for valid data"
    (let [schema {:name s/Str :age s/Int}
          data {:name "John" :age 30}]
      (is (either/right? (validate-with-either schema data))))))

(deftest test-validate-with-either-failure
  (testing "Validation with either returns failure for invalid data"
    (let [schema {:name s/Str :age s/Int}
          data {:name "John" :age "30"}]
      (is (either/left? (validate-with-either schema data))))))
