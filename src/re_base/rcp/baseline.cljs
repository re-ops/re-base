(ns re-base.rcp.baseline
  "Common tools"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.download :refer (download)]))

(defn utils
  "common tools"
  []
  (package "gt5")
  (package "nmap"))
