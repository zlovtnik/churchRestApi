(ns church-api.validation.schemas.user
  (:require [schema.core :as s]
            [church-api.validation.rules :as rules]))

(def CreateUserSchema
  {:email (s/constrained s/Str rules/valid-email?)
   :password (s/constrained s/Str rules/strong-password?)
   :name (s/constrained s/Str rules/valid-name?)
   (s/optional-key :phone) (s/maybe s/Str)
   (s/optional-key :roles) [s/Keyword]})

(def UpdateUserSchema
  {(s/optional-key :email) (s/constrained s/Str rules/valid-email?)
   (s/optional-key :name) (s/constrained s/Str rules/valid-name?)
   (s/optional-key :phone) (s/maybe s/Str)})

(def LoginSchema
  {:email (s/constrained s/Str rules/valid-email?)
   :password s/Str})

(defn validate-create-user [data]
  (s/check CreateUserSchema data))

(defn validate-update-user [data]
  (s/check UpdateUserSchema data))

(defn validate-login [data]
  (s/check LoginSchema data))
