(ns church-api.db.users
  (:require [clojure.java.jdbc :as jdbc]
            [church-api.db.connection :refer [with-db-connection get-datasource]]))

(def users-table-ddl
  (jdbc/create-table-ddl :users
                         [[:id :serial "PRIMARY KEY"]
                          [:username :varchar "NOT NULL UNIQUE"]
                          [:email :varchar "NOT NULL UNIQUE"]
                          [:password_hash :varchar "NOT NULL"]
                          [:first_name :varchar]
                          [:last_name :varchar]
                          [:created_at :timestamp "NOT NULL DEFAULT CURRENT_TIMESTAMP"]
                          [:updated_at :timestamp "NOT NULL DEFAULT CURRENT_TIMESTAMP"]]))

(defn init-users-table []
  (with-db-connection [conn (get-datasource)]
    (jdbc/db-do-commands conn users-table-ddl)
    (println "Users table created.")))

(defn create-user [user-data]
  (with-db-connection [conn (get-datasource)]
    (jdbc/insert! conn :users user-data)))

(defn get-user-by-id [id]
  (with-db-connection [conn (get-datasource)]
    (first (jdbc/query conn ["SELECT * FROM users WHERE id = ?" id]))))

(defn get-user-by-username [username]
  (with-db-connection [conn (get-datasource)]
    (first (jdbc/query conn ["SELECT * FROM users WHERE username = ?" username]))))

(defn get-user-by-email [email]
  (with-db-connection [conn (get-datasource)]
    (first (jdbc/query conn ["SELECT * FROM users WHERE email = ?" email]))))

(defn get-all-users []
  (with-db-connection [conn (get-datasource)]
    (jdbc/query conn ["SELECT * FROM users"])))

(defn update-user [id updates]
  (with-db-connection [conn (get-datasource)]
    (jdbc/update! conn :users updates ["id = ?" id])))

(defn delete-user [id]
  (with-db-connection [conn (get-datasource)]
    (jdbc/delete! conn :users ["id = ?" id])))
