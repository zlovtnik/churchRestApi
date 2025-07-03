(ns church-api.middleware.cors
  (:require [ring.middleware.cors :as cors]))

(defn wrap-cors [handler]
  (cors/wrap-cors handler
    :access-control-allow-origin [#".*"] ; Allow any origin for now
    :access-control-allow-methods [:get :post :put :delete :options]
    :access-control-allow-headers #{"authorization" "content-type"}))
