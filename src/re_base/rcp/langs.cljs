(ns re-base.rcp.langs
  "Setting up programming launguages"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.output :refer (summary)]))

(defn jdk
  "Setting up the jdk"
  []
  (->
   (package "openjdk-8-jdk")
   (summary "jdk done")))

(defn build-essential
  "Setting up build utilities"
  []
  (->
   (package "build-essential")
   (summary "build-essential done")))
