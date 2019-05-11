(ns re-base.recipes.nvim
  "Setting up NeoVim"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package ppa)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.file :refer (symlink directory chown line file)]
   [re-conf.resources.output :refer (summary)]))

(defn nvim
  "Installing Neovim"
  [{:keys [home name]}]
  (->
   (ppa "ppa:neovim-ppa/unstable" :present)
   (package "neovim" :present)
   (chown (<< "~{home}/.local") name name {:recursive true})
   (summary "Neovim install done")))

(defn nodejs-support
  "nodejs neovim support"
  [{:keys [home name]}]
  (let [prefix (<< "/home/~{name}/.npm")
        npmrc (<< "~{home}/.npmrc")]
    (-> (package "npm" :present)
        (exec "/usr/bin/npm" "install" "--prefix" prefix "neovim")
        (exec "/usr/bin/npm" "install" "--prefix" prefix "node-cljfmt")
        (file npmrc :present)
        (line npmrc (<< "prefix = ~{prefix}"))
        (directory (<< "~{home}/bin") :present)
        (symlink (<< "~{prefix}/node_modules/node-cljfmt/bin/cljfmt") (<< "~{home}/bin/cljfmt"))
        (summary "Neovim nodejs"))))

(defn python-support
  "Neovim python support"
  []
  (->
   (package "python3-pip" :present)
   (package "python3-dev" :present)
   (package "python-pip" :present)
   (package "python-dev" :present)
   (exec "/usr/bin/pip3" "install" "--user" "neovim")
   (summary "Neovim python")))

(defn ruby-support
  "nvim ruby support"
  []
  (->
   (package "rubygems" :present)
   (package "ruby2.5-dev" :present)
   (exec "/usr/bin/gem" "install" "neovim")
   (summary "Neovim ruby")))

(defn config
  "Configure nvim"
  [{:keys [home name]}]
  (let [config (<< "~{home}/.config/nvim")]
    (->
     (clone "git://github.com/narkisr/nvim.git" config)
     (chown config name name {:recursive true})
     (summary "Neovim config done"))))

(defn powerline
  "Install powerline"
  [{:keys [home name]}]
  (let [fonts (<< "~{home}/.fonts")
        repo "git://github.com/scotu/ubuntu-mono-powerline.git"]
    (->
     (directory fonts :present)
     (clone repo (<< "~{fonts}/ubuntu-mono-powerline"))
     (chown fonts name name {:recursive true})
     (summary "powerline done"))))

