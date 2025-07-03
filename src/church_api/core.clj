(ns church-api.core
  (:require [church-api.system :as system]
            [church-api.server])
  (:gen-class))

(defn -main
  "Starts the application."
  []
  (system/start))
