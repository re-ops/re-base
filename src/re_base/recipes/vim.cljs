(ns re-base.recipes.vim
  "Setting up VIM"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.download :refer (download checksum)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.archive :refer (untar)]
   [re-conf.resources.file :refer (symlink directory chown)]
   [re-conf.resources.output :refer (summary)]))

(defn vim
  "Installing VIM"
  []
  (->
   (package "vim-nox")
   (summary "vim done")))

(defn dot-vim
  "Configure VIM"
  [{:keys [home name]}]
  (let [dot (<< "~{home}/.vim")]
    (->
     (clone "git://github.com/narkisr/.vim.git" dot)
     (chown dot name name)
     (symlink (<< "~{dot}/.vimrc") (<< "~{home}/.vimrc") :present)
     (summary "dot-vim done"))))

(defn powerline
  "Install powerline"
  [{:keys [home name]}]
  (let [fonts (<< "~{home}/.fonts")
        repo "git://github.com/scotu/ubuntu-mono-powerline.git"]
    (->
     (directory fonts :present)
     (clone repo (<< "~{fonts}/ubuntu-mono-powerline"))
     (chown fonts name)
     (summary "powerline done"))))

