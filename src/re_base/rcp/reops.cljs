(ns re-base.rcp.reops
  "Setting up re-ops access and agent"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.file :refer (copy chmod)]
   [re-conf.resources.user :refer (user)]
   [re-conf.resources.output :refer (summary)]))

(defn reops-user
  "Setting up reops user"
  []
  (->
   (user "re-ops" :present {:home true})
   (summary "re-ops user setup")))

(defn reops-scripts
  "Setting up re-ops scripts"
  []
  (let [prefix "/usr/local/bin"]
    (->
     (copy "resources/reops/apt-cleanup" (<< "~{prefix}/apt-cleanup"))
     (chmod (<< "~{prefix}/apt-cleanup") 755)
     (copy "resources/reops/purge-kernels" (<< "~{prefix}/purge-kernels"))
     (chmod (<< "~{prefix}/purge-kernels") 755)
     (summary "re-ops scripts"))))

(defn re-gent
  "Re-gent support"
  []
  (->
   (package "openjdk-8-jre-headless" :present)
   (summary "re-ops scripts")))
