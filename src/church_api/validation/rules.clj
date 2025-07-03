(ns church-api.validation.rules)

(defn valid-email? [email]
  ;; Placeholder - add real email validation logic
  (and (string? email) (re-matches #".+@.+\..+" email)))

(defn strong-password? [password]
  ;; Placeholder - add real password strength logic (e.g., length, complexity)
  (and (string? password) (> (count password) 8)))

(defn valid-name? [name]
  ;; Placeholder - add real name validation logic
  (and (string? name) (not-empty name)))
