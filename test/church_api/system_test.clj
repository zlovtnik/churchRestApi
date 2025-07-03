(ns church-api.system-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.system :refer [app-config db-spec ds start stop]]))

(deftest system-test
  (testing "System lifecycle functions"
    (is (not (nil? app-config))
        "System config state should be defined")
    (is (not (nil? db-spec))
        "Database spec state should be defined")
    (is (not (nil? ds))
        "Datasource state should be defined")
    (is (not (nil? start))
        "System start function should be defined")
    (is (not (nil? stop))
        "System stop function should be defined")))
