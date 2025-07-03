(ns church-api.db.connection
  (:require [clojure.java.jdbc :as jdbc]
            [environ.core :refer [env]])
  (:import (com.zaxxer.hikari HikariConfig HikariDataSource)))

(defonce datasource (atom nil))

(defn create-datasource []
  (let [config (HikariConfig.)]
    (.setJdbcUrl config (env :database-url "jdbc:postgresql://localhost:5432/church_db?user=postgres&password=postgres"))
    (.setDriverClassName config "org.postgresql.Driver")
    (.setMaximumPoolSize config 10)
    (HikariDataSource. config)))

(defn init-db []
  (reset! datasource (create-datasource))
  (println "Database connection pool initialized."))

(defn get-datasource []
  @datasource)

(defmacro with-db-connection [bindings & body]
  `(jdbc/with-db-connection [~@bindings (get-datasource)]
     ~@body))

(defn close-db []
  (when-let [ds @datasource]
    (.close ds)
    (reset! datasource nil)
    (println "Database connection pool closed.")))
