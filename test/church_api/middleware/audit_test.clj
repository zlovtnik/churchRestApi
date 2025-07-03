(ns church-api.middleware.audit-test
  (:require [clojure.test :refer :all]
            [church-api.middleware.audit :refer :all]))

(deftest test-audit-logging
  (testing "Audit logging middleware logs request and response"
    (let [request {:request-method :get :uri "/api/test"}
          handler (fn [_] {:status 200 :body "OK"})]
      (is (= 200 (:status (wrap-audit-logging handler request)))))))
