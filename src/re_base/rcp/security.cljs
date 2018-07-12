(ns re-base.rcp.security
  "Security utilities"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-base.rcp.hardening :refer (server-harden desktop-harden)]
   [re-conf.resources.facts :refer (desktop?)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.output :refer (summary)]))

(defn packages
  "Security utilities"
  []
  (->
   (package "pwgen")
   (package "rng-tools")
   (package "veracrypt")
   (package "pass")
   (package "nmap")
   (summary "Security packages done")))

(defn hardening
  "Harden os"
  []
  (if (desktop?)
    (desktop-harden)
    (server-harden)))
