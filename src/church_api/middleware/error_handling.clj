(ns church-api.middleware.error-handling
  (:require [ring.util.response :as response]
            [clojure.tools.logging :as log]))

(defn wrap-error-handling [handler]
  (fn [request]
    (try
      (let [response (handler request)]
        (if (and (map? response) 
                 (#{200 201 202 204 301 302 304 400 401 403 404 409 422} (:status response)))
          response
          (do
            (log/error "Unexpected response status or format" {:response response})
            (-> (response/response {:error "Internal Server Error"})
                (response/status 500)))))
      (catch Exception e
        (log/error e "An unexpected error occurred")
        (-> (response/response {:error "Internal Server Error"})
            (response/status 500))))))
