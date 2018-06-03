(ns re-base.rcp.development
  "Setting up dev tools"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update remove])
  (:require
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.archive :refer (untar)]))

(defn intellij
  "Setting up intellij"
  []
  (let [archive "ideaIC-2018.1.4.tar.gz"
        url (<< "https://download.jetbrains.com/idea/~{archive}")
        sha256 "26e674de05976cc7e822d77a2dfe8b8f6136e18f1e91f1c8212019f2781164e1"]
    (->
     (download url "/usr/src/" sha256 :sha256)
     (untar archive "/opt/intallij")
     (summary))))
