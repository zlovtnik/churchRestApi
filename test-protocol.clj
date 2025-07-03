(ns test-protocol
  (:require [cats.protocols :as p]))

;; Print the protocol method names
(defn print-protocol-methods []
  (println "Functor methods:" (keys (methods p/Functor)))
  (println "Applicative methods:" (keys (methods p/Applicative)))
  (println "Monad methods:" (keys (methods p/Monad))))

(print-protocol-methods)
