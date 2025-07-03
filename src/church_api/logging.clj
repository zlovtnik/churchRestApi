(ns church-api.logging
  (:require [taoensso.timbre :as timbre]))

(defn setup-logging []
  (timbre/set-config!
    {:level :info
     :output-fn (partial timbre/default-output-fn {:stacktrace-fonts {}})
     :appenders {:println (timbre/println-appender {:stream :auto})}}))

(defn log-error [error-context error]
  (timbre/error error-context error))

(defn log-info [message]
  (timbre/info message))
