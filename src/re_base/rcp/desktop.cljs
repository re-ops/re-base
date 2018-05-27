(ns re-base.rcp.desktop
  "Desktop setup"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update])
  (:require
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.pkg :refer (package add-repo)]
   [re-conf.resources.output :refer (summary)]))

(defn chrome
  "Google chrome setup"
  []
  (let [repo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main"
        key "https://dl-ssl.google.com/linux/linux_signing_key.pub"]
    (->
     (add-repo repo key "7FAC5991")
     (package "google-chrome-stable")
     (summary "google-chrome install"))))

