(ns re-base.recipes.xfce
 (:require-macros
   [clojure.core.strint :refer (<<)])
 (:require
   [re-conf.core :refer (apply*)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.file :refer (chown copy directory)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.output :refer (summary)]))

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
