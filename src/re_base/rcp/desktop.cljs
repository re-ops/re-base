(ns re-base.rcp.desktop
  "Desktop setup"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update])
  (:require
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.file :refer (chown)]
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

(defn xmonad [{:keys [home uid gid]}]
  (let
   (->
    (package "xmonad" "ghc" "libghc-xmonad-contrib-dev")
    (clone "git://github.com/narkisr/xmonad-config.git" (<< "~{home}/.xmonad"))
    (chown (<< "~{home}/.xmonad") uid gid)
    (exec "/usr/bin/xmonad" "--recompile" :uid uid)
    (summary "xmonad setup"))))
