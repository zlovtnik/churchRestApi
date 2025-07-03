(ns church-api.middleware.authorization)

(defn wrap-authorization
  "A placeholder for authorization middleware.
  In a real app, this would likely take a required permission
  and check if the user has it."
  [handler]
  (fn [request]
    ;; For now, this is a pass-through. Authorization will be handled
    ;; within each handler as per the guide's examples.
    (handler request)))
