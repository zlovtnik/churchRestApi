(ns church-api.routes.users
  (:require [compojure.core :refer [defroutes GET POST PUT]]
            [church-api.handlers.users :as users-handler]))

(defroutes routes
  (GET "/" [] users-handler/get-users)
  (POST "/" [] users-handler/create-user)
  (GET "/:id" [] users-handler/get-user)
  (PUT "/:id" [] users-handler/update-user))
