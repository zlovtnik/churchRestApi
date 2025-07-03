(ns church-api.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defonce config (atom nil))

(defn- read-config [profile]
  (aero/read-config (io/resource "config/config.edn") {:profile profile}))

(defn init [profile]
  (reset! config (read-config profile)))

(defn get-config [& keys]
  (get-in @config keys))
