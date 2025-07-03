(ns church-api.cats.validation
  (:require 
            [cats.monad.either :as either]
            [schema.core :as s]))

(defn validate-with-either [schema data]
  (if-let [errors (s/check schema data)]
    (either/left errors)
    (either/right data)))
