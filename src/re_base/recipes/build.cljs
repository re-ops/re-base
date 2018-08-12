(ns re-base.recipes.build
  "build tools recipes"
  (:require
   [re-conf.resources.download :refer (download checksum)]
   [re-conf.resources.archive :refer (unzip)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.file :refer (chmod)]
   [re-conf.resources.output :refer (summary)]))

(defn packer
  "Setup up packer"
  []
  (let [dest "/tmp/packer_1.2.2_linux_amd64.zip"
        sha "6575f8357a03ecad7997151234b1b9f09c7a5cf91c194b23a461ee279d68c6a8"
        url "https://releases.hashicorp.com/packer/1.2.2/packer_1.2.2_linux_amd64.zip"]
    (->
     (download url dest)
     (checksum dest sha :sha256)
     (unzip dest "/tmp/packger")
     (summary "installing packer done"))))

(defn lein
  "Setting up lein"
  []
  (let [dest "/usr/local/bin/lein"
        url "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein"]
    (->
     (download url dest)
     (chmod dest "0777")
     (summary "lein done"))))
