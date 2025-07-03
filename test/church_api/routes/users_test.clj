(ns church-api.routes.users-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.routes.users :refer [routes]]))

(deftest users-routes-test
  (testing "Users routes configuration"
    (is (not (nil? routes)))
        "Users routes should be defined and return a configuration"))
