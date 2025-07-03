(ns church-api.core
  (:require [church-api.system :as system]
            [church-api.server]
            ;; Explicitly require clj-time.core to ensure it's AOT compiled
            ;; This prevents ClassNotFoundException for clj_time.coerce.ICoerce
            [clj-time.core]
            [clj-time.coerce])
  (:gen-class))

(defn -main
  "Starts the application."
  []
  (system/start))
