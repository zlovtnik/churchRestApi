(ns church-api.security.permissions)

(def role-permissions
  {:admin #{:read-users :create-users :update-users :delete-users}
   :user #{:read-self}})

(defn has-permission? [user required-permission]
  (let [user-roles (get user :roles #{:user})]
    (some (fn [role]
            (let [permissions (get role-permissions role)]
              (contains? permissions required-permission)))
          user-roles)))
