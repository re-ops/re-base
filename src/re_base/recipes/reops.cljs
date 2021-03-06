(ns re-base.recipes.reops
  "Setting up re-ops access and agent"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.file :refer (copy directory chmod template chown)]
   [re-conf.resources.user :refer (user group)]
   [re-conf.resources.output :refer (summary)]))

(defn reops-user
  "Setting up reops user"
  [{:keys [ssh users]}]
  (let [{:keys [name]} (:re-ops users)
        home (<< "/home/~{name}")]
    (->
     (user name :present {:home true})
     (directory (<< "~{home}/.ssh/") :present)
     (template "resources/ssh/authorized_keys.mustache" (<< "~{home}/.ssh/authorized_keys") ssh)
     (chmod (<< "~{home}/.ssh/authorized_keys") "0600")
     (chmod (<< "~{home}/.ssh/") "0700")
     (chown (<< "~{home}/.ssh/") name name {:recursive true})
     (copy "resources/reops/re-ops" "/etc/sudoers.d/re-ops")
     (summary "re-ops user done"))))

(defn reops-scripts
  "Setting up re-ops scripts"
  []
  (let [prefix "/usr/local/bin"]
    (->
     (copy "resources/reops/apt-cleanup" (<< "~{prefix}/apt-cleanup"))
     (chmod (<< "~{prefix}/apt-cleanup") "0755")
     (copy "resources/reops/purge-kernels" (<< "~{prefix}/purge-kernels"))
     (chmod (<< "~{prefix}/purge-kernels") "0755")
     (summary "re-ops scripts done"))))

(defn re-gent
  "Re-gent support"
  []
  (->
   (package "openjdk-8-jre-headless" :present)
   (package "sysstat" :present)
   (summary "re-gent setup done")))
