(ns church-api.middleware.auth
  (:require [church-api.security.jwt :as jwt]
            [church-api.services.user-service :as user-service]))

(defn- get-token [request]
  (when-let [header (get-in request [:headers "authorization"])]
    (second (re-find #"^Bearer (.+)$
" header))))

(defn wrap-authentication [handler]
  (fn [request]
    (let [token (get-token request)
          claims (when token (jwt/validate-token token))]
      (if-let [user (when claims (user-service/get-user (:user-id claims)))]
        (handler (assoc request :user user))
        (handler request)))))
