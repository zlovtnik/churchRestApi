(ns church-api.cats.core
  (:require [cats.core :as m]
            [cats.builtin]
            [cats.protocols :as p]))

(defrecord Result [status body headers]
  p/Functor
  (fmap [_ f]
    (Result. status (f body) headers))
  p/Applicative
  (pure [_ v]
    (Result. 200 v {}))
  p/Monad
  (mreturn [_ v]
    (Result. 200 v {}))
  (mbind [_ f]
    (let [new-result (f body)]
      (if (instance? Result new-result)
        new-result
        (Result. status new-result headers)))))

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

(defrecord Pipeline [run-pipeline]
  p/Functor
  (fmap [_ f]
    (Pipeline. (fn [request]
                 (let [result (run-pipeline request)]
                   (m/fmap f result)))))
  p/Applicative
  (pure [_ v]
    (Pipeline. (fn [_] (success v))))
  p/Monad
  (mreturn [_ v]
    (Pipeline. (fn [_] (success v))))
  (mbind [_ f]
    (Pipeline. (fn [request]
                 (let [result (run-pipeline request)]
                   (if (< (:status result) 400)
                     ((:run-pipeline (f (:body result))) request)
                     result))))))

(defn success? [result]
  (< (:status result) 400))

(defn run-pipeline [pipeline request]
  ((:run-pipeline pipeline) request))
