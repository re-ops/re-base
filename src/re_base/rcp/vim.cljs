(ns re-base.rcp.vim
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
  [{:keys [home uid gid]}]
  (let [dot (<< "~{home}/.vim")]
    (->
     (clone "git://github.com/narkisr/.vim.git" dot)
     (chown dot uid gid)
     (symlink (<< "~{dot}/.vimrc") (<< "~{home}/.vimrc") :present)
     (summary "dot-vim done"))))

(defn bundle
  "Download and setup VIM plugins"
  [{:keys [home uid gid]}]
  (let [dot (<< "~{home}/.vim")
        bundle (<< "~{dot}/bundle")
        version "1.0.0"
        archive (<< "vim-~{version}.tar.gz")
        dest (<< "~{bundle}/~{archive}")
        url (<< "https://github.com/narkisr/.vim/releases/download/v~{version}/~{archive}")
        sha "497e82187930f010e9231abe2d581f3c339dad41400c24abc058fb320cff5f08"]
    (->
     (directory dot :present)
     (download url dest)
     (checksum dest sha :sha256)
     (untar dest bundle)
     (chown bundle uid gid)
     (exec "/usr/bin/rsync" "-a" "--delete"
           (<< "~{bundle}/snipmate-snippets") (<< "{bundle}/snipmate/snippets"))
     (summary "vim bundle done"))))

(defn powerline
  "Install powerline"
  [{:keys [home uid gid]}]
  (let [fonts (<< "~{home}/.fonts")
        repo "git://github.com/scotu/ubuntu-mono-powerline.git"]
    (->
     (directory fonts :present)
     (clone repo (<< "~{fonts}/ubuntu-mono-powerline"))
     (chown fonts uid gid)
     (summary "powerline done"))))

