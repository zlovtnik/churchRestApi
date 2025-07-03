(ns church-api.routes.health-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.routes.health :refer [routes]]))

(deftest health-routes-test
  (testing "Health routes configuration"
    (is (not (nil? routes))
        "Health routes should be defined and return a configuration")))
