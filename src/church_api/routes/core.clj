(ns church-api.routes.core
  (:require [compojure.core :refer [defroutes context]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [church-api.middleware.security-headers :as sec-headers]
            [church-api.middleware.rate-limiting :as rate-limit]
            [church-api.middleware.auth :as auth-middleware]
            [church-api.middleware.authorization :as authz-middleware]
            [church-api.middleware.logging :as logging]
            [church-api.middleware.cors :as cors]
            [church-api.middleware.error-handling :as error-handling]
            [church-api.middleware.ssl :as ssl]
            [church-api.middleware.validation :as validation]
            [church-api.middleware.audit :as audit]
            [church-api.routes.api :as api-routes]
            [church-api.routes.auth :as auth-routes]
            [church-api.routes.health :as health-routes]
            [church-api.cats.core :as cats-core]))

(defroutes app-routes
  (context "/api" []
    (-> api-routes/routes
        (auth-middleware/wrap-authentication)
        (authz-middleware/wrap-authorization)
        (rate-limit/wrap-rate-limiting)
        (validation/wrap-input-validation)
        (audit/wrap-audit-logging)))

  (context "/auth" []
    (-> auth-routes/routes
        (rate-limit/wrap-rate-limiting {:limit 10 :window 60})))

  (context "/health" []
    health-routes/routes)

  (route/not-found {:status 404 :body {:error "Not Found"}}))

(def app
  (-> app-routes
      (sec-headers/wrap-security-headers)
      (cors/wrap-cors)
      (ssl/wrap-ssl-redirect-if-enabled)
      (error-handling/wrap-error-handling)
      (logging/wrap-logging)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-defaults (assoc api-defaults :security {:anti-forgery false}))
      (cats-core/wrap-either->response)))
