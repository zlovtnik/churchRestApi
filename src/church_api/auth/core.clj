(ns church-api.auth.core
  (:require [church-api.services.user-service :as user-service]
            [buddy.hashers :as hashers]))

(defn authenticate [credentials]
  (let [{:keys [email password]} credentials
        user (user-service/get-user-by-email email)]
    (when (and user (hashers/check password (:password-hash user)))
      user)))
