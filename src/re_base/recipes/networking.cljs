(ns re-base.recipes.networking
  "Setting up networking"
  (:require
   [re-conf.resources.file :refer (symlink file)]
   [re-conf.resources.output :refer (summary)]))

(defn dns
  "Setting up DNS in Ubuntu 18.x systems"
  []
  (->
   (file "/etc/resolv.conf" :absent)
   (symlink "/var/run/systemd/resolve/resolv.conf" "/etc/resolv.conf")
   (summary "DNS setup done")))

