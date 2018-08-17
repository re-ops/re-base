(ns re-base.recipes.baseline
  "Common tools"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.download :refer (download)]))

(defn disk-tools
  "disk tools"
  []
  (->
   (package "gt5")
   (summary "disk tools done")))

(defn ssh-tools []
  (->
   (package "sshfs")
   (package "mosh"))
  (summary "ssh tools done"))