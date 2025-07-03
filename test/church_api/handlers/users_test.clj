(ns church-api.handlers.users-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.handlers.users :refer [create-user get-user update-user get-users]]))

(deftest user-handlers-test
  (testing "User creation handler"
    (is (not (nil? create-user))
        "User creation handler should be defined"))
  (testing "User retrieval handler"
    (is (not (nil? get-user))
        "User retrieval handler should be defined"))
  (testing "User update handler"
    (is (not (nil? update-user))
        "User update handler should be defined"))
  (testing "Get all users handler"
    (is (not (nil? get-users))
        "Get all users handler should be defined")))
