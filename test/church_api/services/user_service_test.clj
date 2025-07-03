(ns church-api.services.user-service-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.services.user-service :refer [create-user get-user update-user get-all-users]]))

   (deftest user-service-test
     (testing "User service functions"
       (is (not (nil? create-user))
           "User creation function should be defined")
       (is (not (nil? get-user))
           "User retrieval function should be defined")
       (is (not (nil? update-user))
           "User update function should be defined")
       (is (not (nil? get-all-users))
           "Get all users function should be defined")))
