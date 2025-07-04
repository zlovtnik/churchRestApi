(require '[church-api.security.jwt :as jwt]
         '[church-api.config :as config]
         '[clojure.pprint :refer [pprint]])

;; Initialize config
(config/init :test)

;; Test JWT generation and validation
(let [test-user {:id 1 :email "test@example.com" :roles ["user"]}
      token (jwt/generate-token test-user)
      claims (jwt/validate-token token)]
  (println "Generated token:" token)
  (println "\nValidated claims:")
  (pprint claims)
  (println "\nTest" (if (= (select-keys claims [:user-id :email :roles])
               {:user-id 1 :email "test@example.com" :roles ["user"]})
         "PASSED" "FAILED"))
  
  ;; Test with invalid token
  (println "\nTesting invalid token...")
  (let [invalid-token (str token "x")  ;; Corrupt the token
        invalid-claims (jwt/validate-token invalid-token)]
    (println "Validation result for invalid token:" invalid-claims)
    (println "Test" (if (nil? invalid-claims) "PASSED" "FAILED"))))
