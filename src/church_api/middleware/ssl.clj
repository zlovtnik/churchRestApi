(ns church-api.middleware.ssl
  (:require [ring.middleware.ssl :refer [wrap-ssl-redirect]]
            [church-api.config :as config]))

(defn wrap-ssl-redirect-if-enabled [handler]
  (if (get-in @config/config [:server :ssl?])
    (wrap-ssl-redirect handler)
    handler))
