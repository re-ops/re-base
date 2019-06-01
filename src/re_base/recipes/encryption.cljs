(ns re-base.recipes.encryption
  (:require
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.pkg :refer (package)]))

(defn secure-data
  "secure data tools"
  []
  (->
   (package "pwgen")
   (package "veracrypt")
   (package "pass")
   (summary "security tools done")))


