(ns church-api.server
  (:require [mount.core :as mount]
            [ring.adapter.jetty :as jetty]
            [church-api.routes.core :refer [app]]
            [church-api.config :as config]
            [church-api.logging :as logging]))

(mount/defstate http-server
  :start (let [port (config/get-config :server :port)]
           (logging/setup-logging)
           (logging/log-info (str "Server starting on port: " port))
           (jetty/run-jetty app {:port port :join? false}))
  :stop (.stop http-server))
