(ns re-base.recipes.lxd
  "LXD setup"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (line file)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.output :refer (summary)]))

(defn lxd
  "Installing lxd"
  [{:keys [users]}]
  (->
   (package "zfsutils-linux" "lxd")
   (exec "/usr/sbin/usermod" "-G" "libvirtd" "-a" (-> users :main :name))
   (exec "/usr/bin/lxd" "init" "--auto")
   (summary "lxd")))
