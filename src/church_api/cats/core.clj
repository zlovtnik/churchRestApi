(ns church-api.cats.core
  (:require [cats.protocols :as p]))

;; Simple record types without protocol implementations
(defrecord Result [status body headers]
  p/Printable
  (-repr [this] (str "#Result{" (:status this) " " (:body this) " " (:headers this) "}")))

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

;; Simplified functional implementations without protocols

(defn fmap-result [f result]
  (update result :body f))

(defn pure-result [v]
  (success v))

(defn bind-result [result f]
  (let [new-result (f (:body result))]
    (if (instance? Result new-result)
      new-result
      (assoc result :body new-result))))

(defn fmap-pipeline [f pipeline]
  (Pipeline. (fn [request]
               (f (run-pipeline pipeline request)))))

(defn pure-pipeline [v]
  (Pipeline. (fn [_] (success v))))

(defn bind-pipeline [pipeline f]
  (Pipeline. (fn [request]
               (let [result (run-pipeline pipeline request)]
                 (if (instance? Result result)
                   ((:run-pipeline (f (:body result))) request)
                   ((:run-pipeline (f result)) request))))))