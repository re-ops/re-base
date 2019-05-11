(ns re-base.recipes.osquery
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package ppa)]
   [re-conf.resources.output :refer (summary)]))

(defn osquery
  "Install osquery from repository"
  [])
