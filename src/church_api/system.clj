(ns church-api.system
  (:require [mount.core :as mount]
            [church-api.config :as config]
            [next.jdbc :as jdbc]
            [hikari-cp.core :as hikari]))

(mount/defstate app-config
  :start (config/init (or (System/getenv "APP_PROFILE") "dev")))

(mount/defstate db-spec
  :start (let [{:keys [host dbname user password]} (config/get-config :db)
               jdbc-url (str "jdbc:postgresql://" host "/" dbname)
               ds (hikari/make-datasource {:jdbc-url jdbc-url
                                           :username user
                                           :password password})]
           {:datasource ds}))

(mount/defstate ds
  :start (jdbc/get-datasource db-spec))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))
