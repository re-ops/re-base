(ns re-base.recipes.shell
  "Shell setup recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-base.common :refer (dots)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.shell :refer (unless)]
   [re-conf.spec.file :refer (contains)]
   [re-conf.resources.file :refer (chown directory symlink)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.output :refer (summary)]))

(defn tmux
  "Setup tmux for user"
  [{:keys [home gid uid]}]
  (let [dest (<< "~{home}/.tmux")]
    (->
     (package "tmux")
     (clone "git://github.com/narkisr/.tmux.git" dest)
     (directory (<< "~{dest}/plugins/") :present)
     (clone "git://github.com/tmux-plugins/tpm" (<< "~{dest}/plugins/tpm"))
     (symlink (<< "~{dest}/.tmux.conf") (<< "~{home}/.tmux.conf") :present)
     (chown dest uid gid)
     (clone "git://github.com/narkisr/.tmuxinator.git" (<< "~{home}/.tmuxinator.git"))
     (chown (<< "~{home}/.tmuxinator.git") uid gid)
     (summary "tmux setup done"))))

(defn zsh
  "zsh setup"
  [{:keys [home users]}]
  (let [dest (<< "~{home}/.tmux") {:keys [name]} (users :main)]
    (->
     (package "zsh")
     (contains "/etc/passwd" (<< "~{name}:/bin/zsh"))
     (unless "/usr/bin/chsh" "-s" "/usr/bin/zsh" name)
     (summary "zsh setup done"))))

(defn oh-my-zsh
  "Setup https://github.com/robbyrussell/oh-my-zsh"
  [{:keys [home uid gid]}]
  (let [dest (<< "~{home}/.oh-my-zsh")]
    (->
     (clone "git://github.com/narkisr/oh-my-zsh.git" dest)
     (chown dest uid gid)
     (symlink (<< "~{dest}/.zshrc") (<< "~{home}/.zshrc") :present)
     (summary "oh-my-zsh setup done"))))

(defn dot-files
  "Setting up dot files from git://github.com/narkisr/dots.git"
  [{:keys [home uid gid]}]
  (let [dest (dots home)]
    (->
     (clone "git://github.com/narkisr/dots.git" dest)
     (chown dest uid gid)
     (summary "dot-files setup done"))))

(defn ack
  "ack grep setup"
  [{:keys [home]}]
  (->
   (package "ack")
   (symlink (<< "~(dots home)/.ackrc") (<< "~{home}/.ackrc") :present)
   (summary "ack setup done")))

(defn rlwrap
  "rlwrap setup"
  [{:keys [home]}]
  (->
   (package "rlwrap")
   (symlink (<< "~(dots home)/.inputrc") (<< "~{home}/.inputrc") :present)
   (summary "rlwrap setup done")))
