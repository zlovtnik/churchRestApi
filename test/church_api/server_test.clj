(ns church-api.server-test
  (:require [clojure.test :refer [deftest is testing]]
            [church-api.server :refer [http-server]]))

(deftest server-test
  (testing "Server lifecycle functions"
    (is (not (nil? http-server))
        "Server state should be defined")))
