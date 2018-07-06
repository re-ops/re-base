(ns re-base.rcp.hardening
  "Common hardening"
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.output :refer (summary)]))

(defn common-harden
  "Common hardening logic"
  []
  (package "whoopsie" :purge))

(defn desktop-harden []
  (->
   (common-harden)
   (service "bluetooth" :disable)
   (summary "desktop hardening done")))

(defn server-harden []
  (->
   (common-harden)
   (summary "server hardening done")))
