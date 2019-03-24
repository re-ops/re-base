(ns re-base.recipes.shell
  "Shell setup recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-base.common :refer (dots)]
   [re-conf.resources.pkg :refer (package deb)]
   [re-conf.resources.shell :refer (unless)]
   [re-conf.spec.file :refer (contains)]
   [re-conf.resources.file :refer (chown directory symlink)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.download :refer (download)]
   [re-conf.resources.output :refer (summary)]))

(defn tmux
  "Setup tmux for user"
  [{:keys [home name]}]
  (let [dest (<< "~{home}/.tmux")]
    (->
     (package "tmux")
     (clone "git://github.com/narkisr/.tmux.git" dest)
     (directory (<< "~{dest}/plugins/") :present)
     (clone "git://github.com/tmux-plugins/tpm" (<< "~{dest}/plugins/tpm"))
     (symlink (<< "~{dest}/.tmux.conf") (<< "~{home}/.tmux.conf") :present)
     (chown dest name name)
     (clone "git://github.com/narkisr/.tmuxinator.git" (<< "~{home}/.tmuxinator.git"))
     (chown (<< "~{home}/.tmuxinator.git") name name {:recursive true})
     (summary "tmux setup"))))

(defn zsh
  "zsh setup"
  [{:keys [home users]}]
  (let [dest (<< "~{home}/.tmux") {:keys [name]} (users :main)]
    (->
     (package "zsh")
     (contains "/etc/passwd" (<< "~{name}:/bin/zsh"))
     (unless "/usr/bin/chsh" "-s" "/usr/bin/zsh" name)
     (summary "zsh setup"))))

(defn oh-my-zsh
  "Setup https://github.com/robbyrussell/oh-my-zsh"
  [{:keys [home name]}]
  (let [dest (<< "~{home}/.oh-my-zsh")]
    (->
     (clone "git://github.com/narkisr/oh-my-zsh.git" dest)
     (chown dest name name)
     (symlink (<< "~{dest}/.zshrc") (<< "~{home}/.zshrc") :present)
     (summary "oh-my-zsh setup"))))

(defn dot-files
  "Setting up dot files from git://github.com/narkisr/dots.git"
  [{:keys [home name]}]
  (let [dest (dots home)]
    (->
     (clone "git://github.com/narkisr/dots.git" dest)
     (chown dest name name)
     (summary "dot-files setup"))))

(defn ack
  "ack grep setup"
  [{:keys [home]}]
  (->
   (package "ack")
   (symlink (<< "~(dots home)/.ackrc") (<< "~{home}/.ackrc") :present)
   (summary "ack setup")))

(defn rlwrap
  "rlwrap setup"
  [{:keys [home]}]
  (->
   (package "rlwrap")
   (symlink (<< "~(dots home)/.inputrc") (<< "~{home}/.inputrc") :present)
   (summary "rlwrap setup")))

(defn fd
  "fd a friendly alternative to find"
  []
  (let [version "7.3.0"
        artifact (<< "fd_~{version}_amd64.deb")
        url (<< "https://github.com/sharkdp/fd/releases/download/v~{version}/~{artifact}")]
    (->
     (download url (<< "/tmp/~{artifact}"))
     (package (<< "/tmp/~{artifact}") deb :present)
     (summary "fd"))))

(defn bat
  "bat a modern cat"
  []
  (let [version "0.10.0"
        artifact (<< "bat_~{version}_amd64.deb")
        url (<< "https://github.com/sharkdp/bat/releases/download/v~{version}/~{artifact}")]
    (->
     (download url (<< "/tmp/~{artifact}"))
     (package (<< "/tmp/~{artifact}") deb :present)
     (summary "bat"))))
