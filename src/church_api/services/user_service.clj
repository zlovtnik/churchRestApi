(ns church-api.services.user-service
  (:require [buddy.hashers :as hashers]))

(defonce users (atom {}))

(defn get-all-users []
  (vals @users))

(defn get-user-by-email [email]
  (first (filter #(= email (:email %)) (vals @users))))

(defn get-user [id]
  (@users id))

(defn create-user [user-data]
  (let [user-id (str (java.util.UUID/randomUUID))
        hashed-password (hashers/encrypt (:password user-data))
        new-user (assoc user-data :id user-id :password-hash hashed-password)]
    (swap! users assoc user-id new-user)
    (dissoc new-user :password)))

(defn update-user [id user-data]
  (when-let [existing-user (get-user id)]
    (let [updated-user (merge existing-user user-data)]
      (swap! users assoc id updated-user)
      updated-user)))
