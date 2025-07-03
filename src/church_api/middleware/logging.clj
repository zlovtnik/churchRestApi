(ns church-api.middleware.logging
  (:require [ring.logger :as logger]))

(defn wrap-logging [handler]
  (logger/wrap-with-logger handler))
