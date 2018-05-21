(ns re-base.rcp.backup
  "Backup utilities recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update key remove])
  (:require
   [re-conf.resources.pkg :refer (package repository key update)]
   [re-conf.resources.download :refer (download checksum)]
   [re-conf.resources.pkg :refer (package)]
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
     (download url dest)
     (checksum dest expected :sha256)
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

(defn dropbox-headless
  "Setting up headless dropbox"
  []
  (let [archive "/tmp/dropbox.tar.gz"
        url "https://www.dropbox.com/download?plat=lnx.x86_64"
        dest "/usr/local/dropbox-deamon"]
    (if (desktop?)
      (->
        (download url archive)
        (untar archive "/tmp/")
        (directory dest :present)
        (exec "test" "-d" (<< "~{dest}/.dropbox-dist"))
        (unless "/bin/mv" "/tmp/.dropbox-dist" dest)
        (summary "headless dropbox setup"))
      (->
        (repository "deb http://linux.dropbox.com/ubuntu xenial main" :present)
        (key "pgp.mit.edu" "1C61A2656FB57B7E4DE0F4C1FC918B335044912E")
        (update)
        (package "dropbox")
        (summary "dropbox setup"))
      )))

