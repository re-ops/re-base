(ns re-base.rcp.docker
  "Docker setup and configuration"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (append)]
   [re-conf.resources.output :refer (summary)]))

(defn elasticsearch
  "Setting up elasticsearch docker settings"
  []
  (append "/etc/sysctl.conf" "vm.max_map_count = 262144"))
