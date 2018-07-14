(ns re-base.rcp.hardening
  "Common hardening"
  (:require
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.file :refer (template)]
   [re-conf.resources.output :refer (summary)]))

(defn logwatch
  "Setting up logwatch"
  [{:keys [email]}]
  (->
   (package "ssmtp" :present)
   (package "logwatch" :present)
   (template email "resources/logwatch/ssmtp.mustache" "/etc/ssmtp/ssmtp.conf")
   (summary "logwatch setup done")))

(defn common
  "Common hardening logic"
  []
  (->
   (exec "/usr/sbin/ufw" "allow" "22")
   (exec "/usr/sbin/ufw" "--force" "enable")
   (package "whoopsie" :absent)))

(defn desktop []
  (->
   (common)
   (service "bluetooth" :disable)
   (summary "desktop hardening done")))

(defn server []
  (->
   (common)
   (summary "server hardening done")))

(defn public
  "Interet facing server hardening"
  [env]
  (->
   (logwatch env)
   (common)
   (summary "public hardening done")))
