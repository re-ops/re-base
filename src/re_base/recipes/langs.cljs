(ns re-base.recipes.langs
  "Setting up programming launguages"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.facts :refer (arm?)]
   [re-conf.resources.file :refer (file line directory symlink chmod chown)]
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.archive :refer (unzip)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.pkg :refer (package deb key-server update)]
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
    (let [url "https://d3pxv6yz143wms.cloudfront.net/8.212.04.2"
          artifact "java-1.8.0-amazon-corretto-jdk_8.212.04-2_amd64.deb"]
      (->
       (package "openjdk-8-jdk" :absent)
       (download (<< "~{url}/~{artifact}") (<< "/tmp/~{artifact}"))
       (package (<< "/tmp/~{artifact}") deb :present)
       (summary "Correto JDK 8 done")))))

(defn build-essential
  "Setting up build utilities"
  []
  (->
   (package "build-essential")
   (summary "build-essential done")))

(defn clojure
  "Setting up Clojure tools"
  [{:keys [home name]}]
  (let [install "linux-install-1.10.0.414.sh"
        url (<< "https://download.clojure.org/install/~{install}")
        prefix (<< "~{home}/.clojure")]
    (->
     (download url (<< "/tmp/~{install}"))
     (package "curl")
     (chmod (<< "/tmp/~{install}") "+x")
     (exec (<< "/tmp/~{install}") "--prefix" prefix)
     (directory (<< "~{home}/bin/") :present)
     (symlink (<< "~{prefix}/bin/clojure") (<< "~{home}/bin/clojure") :present)
     (symlink (<< "~{prefix}/bin/clj") (<< "~{home}/bin/clj") :present)
     (chown prefix name name {:recursive true})
     (summary "clojure tools"))))

(defn joker
  "Setting up Joker linter"
  [{:keys [home]}]
  (let [version "0.12.0"
        archive (<< "joker-~{version}-linux-amd64.zip")
        url (<< "https://github.com/candid82/joker/releases/download/v~{version}/~{archive}")]
    (->
     (download url (<< "/tmp/~{archive}"))
     (directory (<< "~{home}/bin/") :present)
     (unzip (<< "/tmp/~{archive}") (<< "~{home}/bin/"))
     (summary "joker"))))
