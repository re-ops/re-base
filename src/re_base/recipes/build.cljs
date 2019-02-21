(ns re-base.recipes.build
  "build tools recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.download :refer (download checksum)]
   [re-conf.resources.archive :refer (unzip)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.file :refer (chmod symlink directory)]
   [re-conf.resources.output :refer (summary)]))

(defn packer
  "Setup up packer"
  []
  (let [dest "/tmp/packer_1.3.1_linux_amd64.zip"
        sha "254cf648a638f7ebd37dc1b334abe940da30b30ac3465b6e0a9ad59829932fa3"
        url "https://releases.hashicorp.com/packer/1.3.1/packer_1.3.1_linux_amd64.zip"]
    (->
     (download url dest)
     (checksum dest sha :sha256)
     (unzip dest "/opt/packer")
     (symlink "/opt/packer/packer" "/usr/local/bin/packer" :present)
     (summary "installing packer done"))))

(defn lein
  "Setting up lein"
  [{:keys [home name]}]
  (let [dest (<< "~{home}/bin/lein")
        url "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein"]
    (->
     (directory (<< "~{home}/bin") :present)
     (download url dest)
     (chmod dest "+x")
     (summary "lein done"))))
