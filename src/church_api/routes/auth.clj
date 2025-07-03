(ns church-api.routes.auth
  (:require [compojure.core :refer [defroutes POST]]
            [church-api.handlers.auth :as auth-handler]))

(defroutes routes
  (POST "/login" [] auth-handler/login)
  (POST "/logout" [] auth-handler/logout)
  (POST "/refresh" [] auth-handler/refresh-token))
