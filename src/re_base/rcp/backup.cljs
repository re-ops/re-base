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
   [re-conf.resources.file :refer (directory)]
   [re-conf.resources.archive :refer (bzip2 untar)]
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.output :refer (summary)]))

(defn restic
  "Setting up restic"
  []
  (let [dest "/tmp/restic_0.8.3_linux_amd64.bz2"
        expected "1e9aca80c4f4e263c72a83d4333a9dac0e24b24e1fe11a8dc1d9b38d77883705"
        url "https://github.com/restic/restic/releases/download/v0.8.3/restic_0.8.3_linux_amd64.bz2"]
    (->
     (download url dest expected :sha256)
     (bzip2 dest)
     (summary "restic setup"))))

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
    (dropbox-headless)))
