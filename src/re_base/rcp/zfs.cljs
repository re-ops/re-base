(ns re_base.rcp.zfs
  "Setting up ZFS filesystem"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.file :refer (copy)]
   [re-conf.resources.output :refer (summary)]))

(defn zfs
  "Base zfs setup"
  []
  (->
   (package "zfsutils-linux")
   (copy "resources/zfs.conf" "/")))
