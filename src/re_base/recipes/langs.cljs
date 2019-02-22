(ns re-base.recipes.langs
  "Setting up programming launguages"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.facts :refer (arm?)]
   [re-conf.resources.file :refer (file line)]
   [re-conf.resources.pkg :refer (package repository key-server update)]
   [re-conf.resources.output :refer (summary)]))

(defn jdk
  "Setting up the jdk"
  []
  (if (arm?)
    (->
     (let [source "/etc/apt/sources.list.d/zulu.list"]
       (key-server "keyserver.ubuntu.com"  "0x219BD9C9")
       (file source :present)
       (line source "deb http://repos.azulsystems.com/debian stable main")
       (update)
       (package "zulu-embedded-8"))
     (summary "Azul embedded JDK done"))
    (->
     (package "openjdk-8-jdk")
     (summary "OpenJDK 8 done"))))

(defn build-essential
  "Setting up build utilities"
  []
  (->
   (package "build-essential")
   (summary "build-essential done")))
