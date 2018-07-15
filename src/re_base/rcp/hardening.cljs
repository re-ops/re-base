(ns re-base.rcp.hardening
  "Common hardening"
  (:require
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.file :refer (template copy)]
   [re-conf.resources.output :refer (summary)]))

(defn network
  "Hardening network"
  []
  (let [target "/etc/sysctl.d/10-network-hardening.conf"]
    (->
     (copy "resources/networking/harden.conf" target)
     (exec "/usr/sbin/ufw" "allow" "22")
     (exec "/sbin/sysctl" "-p" target)
     (exec "/usr/sbin/ufw" "--force" "enable")
     (summary "network harden done"))))

(defn logwatch
  "Setting up logwatch"
  [c {:keys [email]}]
  (->
   (package c "ssmtp" :present)
   (package "logwatch" :present)
   (template email "resources/logwatch/ssmtp.mustache" "/etc/ssmtp/ssmtp.conf")
   (summary "logwatch setup done")))

(defn common
  "Common hardening logic"
  []
  (->
   (network)
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
   (common)
   (logwatch env)
   (summary "public hardening done")))
