(ns church-api.middleware.authorization-test
  (:require [clojure.test :refer :all]
            [church-api.middleware.authorization :refer :all]))

#_(deftest test-check-role
  (testing "Check role middleware allows access with correct role"
    (let [request {:user {:roles ["admin"]}}
          handler (fn [_] {:status 200})
          roles ["admin"]]
      (is (= 200 (:status ((check-role roles) handler request)))))))

#_(deftest test-check-role-denied
  (testing "Check role middleware denies access with incorrect role"
    (let [request {:user {:roles ["user"]}}
          handler (fn [_] {:status 200})
          roles ["admin"]]
      (is (= 403 (:status ((check-role roles) handler request)))))))
