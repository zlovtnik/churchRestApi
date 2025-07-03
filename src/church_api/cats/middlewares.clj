(ns church-api.cats.middlewares
  (:require [cats.core :as m]
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
  (cats/->Pipeline
    (fn [request]
      (let [token (get-in request [:headers "authorization"])]
        (if-let [user (jwt/validate-token token)]
          (cats/success (assoc request :user user))
          (cats/unauthorized {:error "Invalid or missing token"}))))))

(defn authorize [required-permission]
  (cats/->Pipeline
    (fn [request]
      (let [user (:user request)]
        (if (permissions/has-permission? user required-permission)
          (cats/success request)
          (cats/forbidden {:error "Insufficient permissions"}))))))

(defn validate-input [schema]
  (cats/->Pipeline
    (fn [request]
      (let [body (:body request)]
        (m/bind (validation/validate-with-either schema body)
                (fn [validated-data]
                  (cats/success (assoc request :validated-body validated-data))))))))

(defn rate-limit [limit window]
  (cats/->Pipeline
    (fn [request]
      (let [client-id (get-client-id request)]
        (if (rate-limit-exceeded? client-id limit window)
          (cats/->Result 429 {:error "Rate limit exceeded"} {"Retry-After" (str window)})
          (cats/success request))))))

(defn compose-middleware [& middlewares]
  (reduce (fn [acc middleware]
            (cats/->Pipeline
              (fn [request]
                (m/bind (cats/run-pipeline acc request)
                        (fn [processed]
                          (cats/run-pipeline middleware processed))))))
          (cats/->Pipeline (fn [request] (cats/success request)))
          middlewares))
