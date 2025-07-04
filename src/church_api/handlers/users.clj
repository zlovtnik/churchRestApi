(ns church-api.handlers.users
  (:require [ring.util.response :as response]
            [church-api.services.user-service :as user-service]
            [church-api.validation.schemas.user :as user-validation]
            [church-api.security.permissions :as permissions]
            [church-api.security.audit :as audit]
            [church-api.logging :as logging]
            [church-api.cats.core :as cats]
            [cats.monad.either :as either]))

(defn get-users [request]
  (let [user (get-in request [:user])]
    (logging/log-info (str "Getting users list for user: " (:id user)))
    (if (permissions/has-permission? user :read-users)
      (let [users (user-service/get-all-users)]
        (either/right (map #(dissoc % :password-hash) users)))
      (do
        (logging/log-error {:context "get-users" :user-id (:id user)} "Insufficient permissions")
        (either/left {:status 403 :error "Insufficient permissions"})))))

(defn get-user [request]
  (let [user-id (get-in request [:params :id])
        current-user (get-in request [:user])]
    (logging/log-info (str "Getting user " user-id " by user: " (:id current-user)))
    (if (or (= (:id current-user) user-id)
            (permissions/has-permission? current-user :read-users))
      (if-let [user (user-service/get-user user-id)]
        (response/response (dissoc user :password-hash))
        (do
          (logging/log-error {:context "get-user" :user-id user-id} "User not found")
          (response/not-found {:error "User not found"})))
      (do
        (logging/log-error {:context "get-user" :user-id user-id :requester-id (:id current-user)} "Insufficient permissions")
        (response/status 403 {:error "Insufficient permissions"})))))

(defn create-user [request]
  (let [user-data (get-in request [:body])
        current-user (get-in request [:user])]
    (logging/log-info (str "Creating user by: " (or (:id current-user) "unauthenticated user")))
    (if (and current-user (permissions/has-permission? current-user :create-users))
      (if-let [errors (user-validation/validate-create-user user-data)]
        (do
          (logging/log-error {:context "create-user" :data user-data} (str "Validation errors: " errors))
          (response/bad-request {:errors errors}))
        (try
          (let [new-user (user-service/create-user user-data)]
            (audit/log-user-creation new-user current-user request)
            (response/created (str "/api/users/" (:id new-user)) (dissoc new-user :password-hash)))
          (catch Exception e
            (logging/log-error {:context "create-user" :data user-data} (str "Unexpected error: " (.getMessage e)))
            (response/status 500 {:error "Internal Server Error"}))))
      (if current-user
        (do
          (logging/log-error {:context "create-user" :requester-id (:id current-user)} "Insufficient permissions")
          (response/status 403 {:error "Insufficient permissions"}))
        (do
          (logging/log-error {:context "create-user"} "Unauthenticated")
          (response/status 401 {:error "Unauthenticated"}))))))

(defn update-user [request]
  (let [user-id (get-in request [:params :id])
        user-data (get-in request [:body])
        current-user (get-in request [:user])]
    (logging/log-info (str "Updating user " user-id " by user: " (:id current-user)))
    (if (or (= (:id current-user) user-id)
            (permissions/has-permission? current-user :update-users))
      (if-let [errors (user-validation/validate-update-user user-data)]
        (do
          (logging/log-error {:context "update-user" :data user-data} (str "Validation errors: " errors))
          (response/bad-request {:errors errors}))
        (try
          (let [updated-user (user-service/update-user user-id user-data)]
            (audit/log-user-update updated-user current-user request)
            (response/response updated-user))
          (catch Exception e
            (logging/log-error {:context "update-user" :user-id user-id} (str "Unexpected error: " (.getMessage e)))
            (response/status 500 {:error "Internal Server Error"}))))
      (do
        (logging/log-error {:context "update-user" :user-id user-id :requester-id (:id current-user)} "Insufficient permissions")
        (response/status 403 {:error "Insufficient permissions"})))))

;;todo need to create delete!!!!
