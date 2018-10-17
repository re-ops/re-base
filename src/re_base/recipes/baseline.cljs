(ns re-base.recipes.baseline
  "Common tools"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (line)]
   [re-conf.resources.shell :refer (exec)]
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
   (package "mosh")
   (summary "ssh tools done")))

(defn inotify-max
  "Increasing max inotify watches"
  []
  (->
   (line "/etc/sysctl.conf" "fs.inotify.max_user_watches = 1048576")
   (exec "/sbin/sysctl" "-p")
   (summary "inotify-max done")))
