(ns church-api.middleware.audit
  (:require [clojure.tools.logging :as log]))

(defn wrap-audit-logging [handler]
  (fn [request]
    (let [response (handler request)]
      ;; In a real app, you would inspect the request and response
      ;; to determine if an auditable event occurred and log it.
      ;; For example, log all POST, PUT, DELETE requests.
      (when (#{:post :put :delete} (:request-method request))
        (log/info (str "Audit: "
                       (get-in request [:user :email] "anonymous")
                       " performed "
                       (name (:request-method request))
                       " on "
                       (:uri request)
                       " -> "
                       (:status response))))
      response)))
