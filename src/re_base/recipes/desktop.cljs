(ns re-base.recipes.desktop
  "Desktop setup"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:refer-clojure :exclude [update])
  (:require
   [re-conf.core :refer (apply*)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.file :refer (chown copy directory)]
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

(defn xmonad [{:keys [home name]}]
  (->
   (package "xmonad" "ghc" "libghc-xmonad-contrib-dev" "gnome-terminal")
   (clone "git://github.com/narkisr/xmonad-config.git" (<< "~{home}/.xmonad"))
   (chown (<< "~{home}/.xmonad") name name)
   (exec "/usr/bin/xmonad" "--recompile")
   (summary "xmonad setup")))

(defn xfce
  [{:keys [home]}]
  (let [file "xfce4-keyboard-shortcuts.xml"
        shortcuts (<< "resources/xfce/~{file}")
        dest (<< "~{home}/.config/xfce4/xfconf/xfce-perchannel-xml/~{file}")
        cleanup ["Music" "Pictures" "Public" "Templates" "Videos"]]
    (->
     (copy shortcuts dest)
     (apply* directory (fn [d] [(<< "~{home}/~{d}") :absent]) cleanup)
     (summary "xfce setup done"))))
