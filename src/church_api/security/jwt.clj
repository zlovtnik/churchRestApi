(ns church-api.security.jwt
  (:require [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [church-api.config :as config]))

(defn- get-secret []
  (let [secret (config/get-config :jwt :secret)]
    (println "[DEBUG] JWT secret in use:" secret)
    secret))

(defn generate-token [user]
  (let [exp (time/plus (time/now) (time/hours 1))
        claims {:user-id (:id user)
                :email (:email user)
                :roles (:roles user)
                :exp (coerce/to-epoch exp)
                :iat (coerce/to-epoch (time/now))
                :iss "church-api"}]
    (jwt/sign claims (get-secret) {:alg :hs256})))

(defn validate-token [token]
  (try
    (let [claims (jwt/unsign token (get-secret) {:alg :hs256})]
      (when (> (:exp claims) (coerce/to-epoch (time/now)))
        claims))
    (catch Exception e
      (println "Token validation error:" (.getMessage e))
      nil)))

(defn generate-refresh-token [user]
  (let [exp (time/plus (time/now) (time/days 30))
        claims {:user-id (:id user)
                :type :refresh
                :exp (coerce/to-epoch exp)
                :iat (coerce/to-epoch (time/now))}]
    (jwt/sign claims (get-secret) {:alg :hs256})))

(defn revoke-token [token]
  ;; Add to blacklist in Redis or database
  (when token
    ;; Implementation depends on your blacklist strategy
    (println "Revoking token:" token)
    ))
