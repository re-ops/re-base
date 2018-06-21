(ns re-base.rcp.reops
  "Setting up re-ops access and agent"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.file :refer (copy)]
   [re-conf.resources.user :refer (user)]
   [re-conf.resources.output :refer (summary)]))
