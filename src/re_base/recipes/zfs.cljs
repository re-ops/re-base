(ns re-base.recipes.zfs
  "Setting up ZFS filesystem"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.file :refer (copy)]
   [re-conf.resources.user :refer (user)]
   [re-conf.resources.output :refer (summary)]))

(defn zfs
  "Base zfs setup and tunning"
  []
  (->
   (package "zfsutils-linux")
   (copy "resources/zfs/sudo-zfs" "/etc/sudoers.d/zfs")
   (copy "resources/zfs/zfs.conf" "/etc/modprobe.d/zfs.conf")
   (summary "zfs tunning done")))

(defn zfs-user
  "allow running zfs commands as non root user or as passwordless sudo see:
       https://github.com/zfsonlinux/zfs/issues/362"
  [{:keys [users]}]
  (->
   (copy "resources/zfs/zfs.conf" "/etc/modprobe.d/zfs.conf")
   (copy "resources/zfs/91-zfs-permissions.rules" "/etc/udev/rules.d/91-zfs-permissions.rules")
   (user "zfs" :present {:home false})
   (exec "/usr/sbin/usermod" "-G" "zfs" "-a" (-> users :main :name))
   (summary "zfs user done")))
