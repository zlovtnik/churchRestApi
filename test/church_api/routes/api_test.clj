(ns church-api.routes.api-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.routes.api :refer [routes]]))

(deftest api-routes-test
  (println "Starting api-routes-test")
  (testing "API routes are defined"
    (is (not (nil? routes))
        "API routes should be defined")))
