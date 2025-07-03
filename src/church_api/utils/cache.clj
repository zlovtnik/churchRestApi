(ns church-api.utils.cache
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]))

(defonce cache (atom {}))

(defn cache-get [key default-value]
  (let [entry (get @cache key)]
    (if (and entry (> (:expires-at entry) (coerce/to-epoch (time/now))))
      (:value entry)
      (do
        (swap! cache dissoc key)
        default-value))))

(defn cache-set [key value ttl-seconds]
  (let [expires-at (coerce/to-epoch (time/plus (time/now) (time/seconds ttl-seconds)))]
    (swap! cache assoc key {:value value :expires-at expires-at})))
