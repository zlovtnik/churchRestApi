(ns church-api.routes.health
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :as response]))

(defroutes routes
  (GET "/" [] (response/response {:status "ok"})))
