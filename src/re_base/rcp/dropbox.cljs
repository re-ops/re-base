(ns re-base.rcp.dropbox
  "Setting up dropbox"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update remove])
  (:require
   [re-conf.resources.pkg :refer (package repository key-server update)]
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.archive :refer (untar)]
   [re-conf.resources.file :refer (directory)]
   [re-conf.resources.shell :refer (exec unless)]
   [re-conf.resources.output :refer (summary)]))

(defn dropbox-headless
  "Setting up headless dropbox"
  [{:keys [home]}]
  (let [archive "/tmp/dropbox.tar.gz"
        url "https://www.dropbox.com/download?plat=lnx.x86_64"
        py_url "https://www.dropbox.com/download?dl=packages/dropbox.py"
        py_bin (<< "~{home}/bin/dropbox.py")
        dest "/usr/local/dropbox-deamon"]
    (->
     (download url archive)
     (untar archive "/tmp/")
     (directory dest :present)
     (exec "test" "-d" (<< "~{dest}/.dropbox-dist"))
     (unless "/bin/mv" "/tmp/.dropbox-dist" dest)
     (directory (<< "~{home}/bin") :present)
     (download py_url py_bin)
     (summary "headless dropbox setup"))))

(defn dropbox-desktop
  []
  (->
   (key-server "pgp.mit.edu" "1C61A2656FB57B7E4DE0F4C1FC918B335044912E")
   (repository "deb http://linux.dropbox.com/ubuntu xenial main" :present)
   (update)
   (package "dropbox")
   (summary "desktop dropbox setup")))
