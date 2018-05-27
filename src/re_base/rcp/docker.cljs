(ns re-base.rcp.docker
  "Docker setup and configuration"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update])
  (:require
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.file :refer (line chmod)]
   [re-conf.resources.pkg :refer (install package)]
   [re-conf.resources.output :refer (summary)]))

(defn elasticsearch
  "Setting up elasticsearch docker settings"
  []
  (->
   (line "/etc/sysctl.conf" "vm.max_map_count = 262144")
   (summary "docker elasticsearch setup")))

(defn docker
  []
  (let [repo "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic edge"
        url "https://download.docker.com/linux/ubuntu/gpg"]
    (->
     (install repo url "0EBFCD88")
     (package "docker-ce")
     (summary "docker-ce install"))))

(defn compose
  "Setting up docker-compose"
  []
  (let [url "https://github.com/docker/compose/releases/download/1.21.2/docker-compose-Linux-x86_64"
        dest "/usr/local/bin/docker-compose"
        expected "8a11713e11ed73abcb3feb88cd8b5674b3320ba33b22b2ba37915b4ecffdf042"]
    (->
     (download url dest expected :sha256)
     (chmod dest "0777")
     (summary "docker compsoe setup"))))
