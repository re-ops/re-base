(ns re-base.recipes.security
  "Security utilities"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (template copy line)]
   [re-conf.resources.firewall :refer (rule firewall)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.facts :refer (desktop?)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.output :refer (summary)]))

(defn scanning
  "scanning tools"
  []
  (->
   (package "nmap")
   (summary "security tools done")))

(defn custom
  "Desktop and public server Hardening"
  [{:keys [public] :as env}]
  (if (desktop?)
    (->
     (service "bluetooth" :disable)
     (summary "desktop hardening"))
    (if public
      (->
       (package "ssmtp" :present)
       (package "logwatch" :present)
       (template "resources/logwatch/ssmtp.mustache" "/etc/ssmtp/ssmtp.conf" email)
       (summary "logwatch setup")))))

(defn sshd
  "harden ssh configuration"
  []
  (->
   (line "/etc/ssh/sshd_config" "PermitRootLogin" "no" " " :set)
   (line "/etc/ssh/sshd_config" "PasswordAuthentication" "no" " " :set)
   (line "/etc/ssh/sshd_config" "X11Forwarding" "no" " " :set)
   (line "/etc/ssh/sshd_config" "\nUseDns no" :present)
   (service "ssh" :restart)
   (summary "ssh hardening")))

(defn network
  "Hardening network"
  []
  (let [target "/etc/sysctl.d/10-network-hardening.conf"]
    (->
     (copy "resources/networking/harden.conf" target)
     (rule {:port 22})
     (firewall :present)
     (exec "/sbin/sysctl" "-p" target)
     (summary "network hardened"))))

(defn privacy
  "privacy cleanup"
  []
  (->
   (package "whoopsie" :absent)
   (package "popularity-contest" :absent)
   (summary "privacy")))

(defn entropy
  "Increasing entropy"
  []
  (->
   (package "rng-tools")
   (summary "entropy increase")))
