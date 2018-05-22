(ns re-base.rcp.backup
  "Backup utilities recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update key remove])
  (:require
   [re-conf.resources.pkg :refer (package repository key update)]
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

(defn dropbox-headless
  "Setting up headless dropbox"
  [{:keys [home]}]
  (let [archive "/tmp/dropbox.tar.gz"
        url "https://www.dropbox.com/download?plat=lnx.x86_64"
        py_url "https://www.dropbox.com/download?dl=packages/dropbox.py"
        py_bin (<< "~{home}/bin/dropbox.py")
        dest "/usr/local/dropbox-deamon"]
    (if (desktop?)
      (->
       (repository "deb http://linux.dropbox.com/ubuntu xenial main" :present)
       (key "pgp.mit.edu" "1C61A2656FB57B7E4DE0F4C1FC918B335044912E")
       (update)
       (package "dropbox")
       (summary "dropbox setup"))
      (->
       (download url archive)
       (untar archive "/tmp/")
       (directory dest :present)
       (exec "test" "-d" (<< "~{dest}/.dropbox-dist"))
       (unless "/bin/mv" "/tmp/.dropbox-dist" dest)
       (directory (<< "~{home}/bin") :present)
       (download py_url py_bin "d7e01a4d178674f1895dc3f74adb7f36" :md5)
       (summary "headless dropbox setup")))))

