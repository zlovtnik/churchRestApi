(ns church-api.middleware.validation
  (:require [schema.core :as s]
            [ring.util.response :as response]))

(defn wrap-input-validation [handler]
  (fn [request]
    (if-let [schema (get-in request [:route-params :validation-schema])]
      (let [errors (s/check schema (:body request))]
        (if errors
          (response/bad-request {:errors errors})
          (handler request)))
      (handler request))))
