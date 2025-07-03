(ns church-api.cats.core
  (:require [cats.core :as m]
            [cats.builtin]
            [cats.protocols :as p]))

;; Helper functions without protocol implementations
(defrecord Result [status body headers])

(defn success
  ([body] (Result. 200 body {}))
  ([body headers] (Result. 200 body headers)))

(defn created
  ([body] (Result. 201 body {}))
  ([body headers] (Result. 201 body headers)))

(defn bad-request
  ([body] (Result. 400 body {}))
  ([body headers] (Result. 400 body headers)))

(defn unauthorized
  ([body] (Result. 401 body {}))
  ([body headers] (Result. 401 body headers)))

(defn forbidden
  ([body] (Result. 403 body {}))
  ([body headers] (Result. 403 body headers)))

(defn not-found
  ([body] (Result. 404 body {}))
  ([body headers] (Result. 404 body headers)))

(defn server-error
  ([body] (Result. 500 body {}))
  ([body headers] (Result. 500 body headers)))

(defn result->response [result]
  {:status  (:status result)
   :body    (:body result)
   :headers (:headers result)})

(defrecord Pipeline [run-pipeline])

(defn success? [result]
  (< (:status result) 400))

(defn run-pipeline [pipeline request]
  ((:run-pipeline pipeline) request))