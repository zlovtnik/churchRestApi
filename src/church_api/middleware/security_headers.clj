(ns church-api.middleware.security-headers
  (:require [ring.util.response :as response]))

(defn wrap-security-headers [handler]
  (fn [request]
    (let [response (handler request)]
      (-> response
          (response/header "X-Frame-Options" "DENY")
          (response/header "X-Content-Type-Options" "nosniff")
          (response/header "X-XSS-Protection" "1; mode=block")
          (response/header "Strict-Transport-Security" 
                          "max-age=31536000; includeSubDomains")
          (response/header "Content-Security-Policy"
                          "default-src 'self'; script-src 'self' 'unsafe-inline'")
          (response/header "Referrer-Policy" "strict-origin-when-cross-origin")
          (response/header "Feature-Policy"
                          "geolocation 'none'; camera 'none'; microphone 'none'")))))
