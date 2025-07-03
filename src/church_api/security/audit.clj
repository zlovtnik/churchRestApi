(ns church-api.security.audit
  (:require [clojure.tools.logging :as log]))

(defn log-authentication-success [user request]
  (log/info (str "Successful login for user: " (:email user) " from IP: " (:remote-addr request))))

(defn log-authentication-failure [credentials request]
  (log/warn (str "Failed login attempt for user: " (:email credentials) " from IP: " (:remote-addr request))))

(defn log-logout [user request]
  (log/info (str "User logged out: " (:email user) " from IP: " (:remote-addr request))))

(defn log-user-creation [created-user current-user request]
  (log/info (str "User " (:email current-user) " created new user " (:email created-user) " from IP: " (:remote-addr request))))

(defn log-user-update [updated-user current-user request]
  (log/info (str "User " (:email current-user) " updated user " (:email updated-user) " from IP: " (:remote-addr request))))
