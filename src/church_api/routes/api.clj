(ns church-api.routes.api
  (:require [compojure.core :refer [defroutes context]]
            [church-api.routes.users :as users-routes]))

(defroutes routes
  (context "/users" [] users-routes/routes))
