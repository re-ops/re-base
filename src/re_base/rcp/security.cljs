(ns re-base.rcp.security
  "Security utilities"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-base.rcp.hardening :as hard]
   [re-conf.resources.facts :refer (desktop?)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.output :refer (summary)]))

(defn security-tools
  "security tools"
  []
  (->
   (package "pwgen")
   (package "rng-tools")
   (package "veracrypt")
   (package "pass")
   (package "nmap")
   (summary "security tools done")))

(defn hardening
  "Harden os"
  [{:keys [public] :as env}]
  (if (desktop?)
    (hard/desktop)
    (if public
      (hard/public env)
      (hard/server))))
