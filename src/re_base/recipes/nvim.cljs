(ns re-base.recipes.nvim
  "Setting up NeoVim"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.download :refer (download checksum)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.archive :refer (untar)]
   [re-conf.resources.file :refer (symlink directory chown line file)]
   [re-conf.resources.output :refer (summary)]))

(defn nvim
  "Installing Neovim"
  []
  (->
   (let [version "0.3.4"
         release "nvim-linux64"
         dest (<< "/opt/~{release}")
         url (<< "https://github.com/neovim/neovim/releases/download/v~{version}/~{release}.tar.gz")
         tmp (<< "/tmp/~{release}.tar.gz")
         expected "e28e6eeb2ebee0fdec22fd0a6bfad6f88440b6fe88823359ef6589c1fc2359fe"]
     (->
      (download url tmp expected :sha256)
      (untar tmp "/opt")
      (symlink (<< "/opt/~{release}/bin/nvim") "/usr/bin/vim")
      (summary "Neovim install done")))))

(defn lang-support [{:keys [home user]}]
  (let [prefix "/home/~{user}/.npm"
        npmrc (<< "~{home}/.npmrc")]
    (->
     (package "python3-pip" :present)
     (exec "/usr/bin/pip3" "install" "--user" "neovim")
     (file npmrc :present)
     (line npmrc (<< "prefix = ~{prefix}"))
     (package "npm" :present)
     (exec "/usr/bin/npm" "install" "--prefix" prefix "neovim")
     (exec "/usr/bin/npm" "install" "--prefix" prefix "node-cljfmt")
     (summary "Neovim lang support done"))))

(defn config
  "Configure nvim"
  [{:keys [home name]}]
  (let [config (<< "~{home}/.config/nvim")]
    (->
     (clone "git://github.com/narkisr/nvim.git" config)
     (chown config name name)
     (summary "Neovim config done"))))

(defn powerline
  "Install powerline"
  [{:keys [home name]}]
  (let [fonts (<< "~{home}/.fonts")
        repo "git://github.com/scotu/ubuntu-mono-powerline.git"]
    (->
     (directory fonts :present)
     (clone repo (<< "~{fonts}/ubuntu-mono-powerline"))
     (chown fonts name name)
     (summary "powerline done"))))

