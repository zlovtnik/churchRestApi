(ns church-api.handlers.auth
  (:require [church-api.auth.core :as auth]
            [church-api.security.jwt :as jwt]
            [church-api.validation.schemas.user :as user-validation]
            [church-api.security.audit :as audit]
            [cats.monad.either :as either]))

(defn login [request]
  (println "LOGIN HANDLER CALLED with" (pr-str request))
  (try
    (let [credentials (get-in request [:body])]
      (println "LOGIN credentials:" (pr-str credentials))
      (if-let [errors (user-validation/validate-login credentials)]
        (do (println "LOGIN validation errors:" (pr-str errors))
            (either/left {:status 400 :errors errors}))
        (if-let [user (auth/authenticate credentials)]
          (let [token (jwt/generate-token user)
                refresh-token (jwt/generate-refresh-token user)]
            (audit/log-authentication-success user request)
            (println "LOGIN success for user:" (:email user))
            (either/right {:token token
                          :refresh-token refresh-token
                          :user (dissoc user :password-hash)
                          :expires-in 3600}))
          (do
            (audit/log-authentication-failure credentials request)
            (println "LOGIN failed: Invalid credentials")
            (either/left {:status 401 :error "Invalid credentials"})))))
    (catch Exception e
      (println "LOGIN ERROR:" (.getMessage e))
      (either/left {:status 500 :error "Internal Server Error"}))))

(defn refresh-token [request]
  (let [auth-header (get-in request [:headers "authorization"])
        refresh-token (when auth-header (second (re-find #"^Bearer (.+)$" auth-header)))]
    (if-let [user (jwt/validate-token refresh-token)]
      (let [new-token (jwt/generate-token user)]
        (either/right {:token new-token
                      :expires-in 3600}))
      (either/left {:status 401 :error "Invalid refresh token"}))))

(defn logout [request]
  (let [user (get-in request [:user])
        auth-header (get-in request [:headers "authorization"])
        token (when auth-header (second (re-find #"^Bearer (.+)$
" auth-header)))]
    (jwt/revoke-token token)
    (audit/log-logout user request)
    (either/right {:message "Logged out successfully"})))
