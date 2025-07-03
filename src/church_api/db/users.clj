(ns church-api.db.users
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [church-api.db.connection :refer [get-datasource]]))

(defn init-users-table []
  (let [ds (get-datasource)]
    (jdbc/execute-one! ds
                     ["CREATE TABLE IF NOT EXISTS users (
                      id SERIAL PRIMARY KEY,
                      username VARCHAR NOT NULL UNIQUE,
                      email VARCHAR NOT NULL UNIQUE,
                      password_hash VARCHAR NOT NULL,
                      first_name VARCHAR,
                      last_name VARCHAR,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )"])
    (println "Users table created.")))

(defn create-user [user-data]
  (let [ds (get-datasource)]
    (sql/insert! ds :users user-data)))

(defn get-user-by-id [id]
  (let [ds (get-datasource)]
    (jdbc/execute-one! ds ["SELECT * FROM users WHERE id = ?" id])))

(defn get-user-by-username [username]
  (let [ds (get-datasource)]
    (jdbc/execute-one! ds ["SELECT * FROM users WHERE username = ?" username])))

(defn get-user-by-email [email]
  (let [ds (get-datasource)]
    (jdbc/execute-one! ds ["SELECT * FROM users WHERE email = ?" email])))

(defn get-all-users []
  (let [ds (get-datasource)]
    (jdbc/execute! ds ["SELECT * FROM users"])))

(defn update-user [id updates]
  (let [ds (get-datasource)]
    (sql/update! ds :users updates {:id id})))

(defn delete-user [id]
  (let [ds (get-datasource)]
    (sql/delete! ds :users {:id id})))
