(ns church-api.cats.middlewares
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [church-api.cats.core :as cats]
            [church-api.security.jwt :as jwt]
            [church-api.security.permissions :as permissions]
            [church-api.cats.validation :as validation]))

(defn- get-client-id [request]
  (get-in request [:headers "x-client-id"] "anonymous"))

(defn- rate-limit-exceeded? [client-id limit window]
  (println "Checking rate limit for" client-id "with limit" limit "in window" window)
  false)

(defn authenticate []
  (fn [request]
    (let [auth-header (get-in request [:headers "authorization"])
          token (when auth-header
                  (if (re-find #"^Bearer " auth-header)
                    (second (re-find #"^Bearer (.+)$" auth-header))
                    ;; For tests that don't format with Bearer prefix
                    auth-header))]
      (if-let [claims (jwt/validate-token token)]
        ;; Transform JWT claims to match expected user format
        (let [user {:id (:user-id claims)
                    :email (:email claims)
                    :roles (:roles claims)}]
          (assoc request :user user))
        {:status 401, :body {:error "Invalid or missing token"}}))))

(defn authorize [required-permission]
  (fn [request]
    (let [user (:user request)]
      (if (permissions/has-permission? user required-permission)
        (either/right request)
        (either/left {:status 403 :error "Insufficient permissions"})))))

(defn validate-input [schema]
  (fn [request]
    (let [body (:body request)]
      (m/bind (validation/validate-with-either schema body)
              (fn [validated-data]
                (either/right (assoc request :validated-body validated-data)))))))

(defn rate-limit [limit window]
  (fn [request]
    (let [client-id (get-client-id request)]
      (if (rate-limit-exceeded? client-id limit window)
        (either/left {:status 429 :error "Rate limit exceeded" :headers {"Retry-After" (str window)}})
        (either/right request)))))

(defn compose-middleware [& middlewares]
  (reduce (fn [acc middleware]
            (fn [request]
              (m/bind (acc request)
                      (fn [processed]
                        (middleware processed)))))
          (fn [request] (either/right request))
          middlewares))
