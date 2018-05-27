(ns re-base.rcp.desktop
  "Desktop setup"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update])
  (:require
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.pkg :refer (package install)]
   [re-conf.resources.output :refer (summary)]))

(defn chrome
  "Google chrome setup"
  []
  (let [repo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main"
        key "https://dl-ssl.google.com/linux/linux_signing_key.pub"]
    (->
     (install repo key "")
     (package "google-chrome-stable")
     (summary "google-chrome install"))))

(defn xmonad
  "Setting up Xmonad"
  [])
