(ns re-base.rcp.backup
  "Backup utilities recipes"
  (:refer-clojure :exclude [update key remove])
  (:require
   [re-conf.resources.download :refer (download checksum)]
   [re-conf.resources.pkg :refer (package repository key update)]
   [re-conf.resources.archive :refer (bzip2)]
   [re-conf.resources.shell :refer (exec)]
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
   (package "openjdk-8-jre")
   (repository "https://raw.githubusercontent.com/narkisr/fpm-barbecue/repo/packages/ubuntu/" :present)
   (key "keyserver.ubuntu.com" "42ED3C30B8C9F76BC85AC1EC8B095396E29035F0")
   (update)
   (package "octo" :present)
   (summary "octo setup")))
