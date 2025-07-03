(ns church-api.middleware.rate-limiting
  (:require [ring.util.response :as response]
            [church-api.utils.cache :as cache]))

(defn get-client-id [request]
  (or (get-in request [:user :id])
      (get-in request [:headers "x-forwarded-for"])
      (get-in request [:remote-addr])))

(defn rate-limit-key [client-id endpoint]
  (str "rate-limit:" client-id ":" endpoint))

(defn wrap-rate-limiting
  ([handler] (wrap-rate-limiting handler {:limit 100 :window 60}))
  ([handler {:keys [limit window]}]
   (fn [request]
     (let [client-id (get-client-id request)
           endpoint (:uri request)
           key (rate-limit-key client-id endpoint)
           current-count (cache/cache-get key 0)]
       (if (>= current-count limit)
         (-> (response/response {:error "Rate limit exceeded"})
             (response/status 429)
             (response/header "Retry-After" window))
         (do
           (cache/cache-set key (inc current-count) window)
           (handler request)))))))
