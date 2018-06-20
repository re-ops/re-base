(ns re-base.rcp.backup
  "Backup utilities recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update remove])
  (:require
   [re-base.rcp.dropbox :refer (dropbox-desktop dropbox-headless)]
   [re-conf.resources.pkg :refer (package repository key-server update)]
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.facts :refer (desktop?)]
   [re-conf.resources.file :refer (directory symlink chmod rename)]
   [re-conf.resources.archive :refer (bzip2 untar)]
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.output :refer (summary)]))

(defn restic
  "Setting up restic"
  []
  (let [version "0.9.1"
        release (<< "restic_~{version}_linux_amd64")
        tmp (<< "/tmp/~{release}.bz2")
        expected "f7f76812fa26ca390029216d1378e5504f18ba5dde790878dfaa84afef29bda7"
        url (<< "https://github.com/restic/restic/releases/download/v~{version}/~{release}.bz2")]
    (->
     (download url tmp expected :sha256)
     (bzip2 tmp)
     (rename (<< "/tmp/~{release}") "/usr/bin/restic")
     (chmod "/usr/bin/restic" 755)
     (summary "restic setup done"))))

(defn octo
  "Setting up Octo"
  []
  (->
   (package "openjdk-8-jre" :present)
   (package "octo" :present)
   (summary "octo setup")))

(defn zbackup
  "Setting up zbackup"
  []
  (->
   (package "zbackup" :present)
   (summary "zbackup setup")))

(defn rclone
  "Setting up rclone"
  []
  (->
   (package "rclone" :present)
   (summary "rclone setup")))

(defn dropbox
  [{:keys [home]}]
  (if (desktop?)
    (dropbox-desktop)
    (dropbox-headless home)))
