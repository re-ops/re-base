(ns re-base.rcp.shell
  "Shell setup recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.cljs.log :refer (info debug error)]
   [re-conf.cljs.pkg :refer (package)]
   [re-conf.cljs.shell :refer (unless)]
   [re-conf.cljs.file :refer (chown directory symlink contains)]
   [re-conf.cljs.git :refer (clone)]
   [re-conf.cljs.shell :refer (exec)]
   [re-conf.cljs.output :refer (summary)]))

(defn tmux
  "Setup tmux for user"
  [{:keys [home gid uid]}]
  (let [dest (<< "~{home}/.tmux")]
    (->
     (package "tmux")
     (clone "git://github.com/narkisr/.tmux.git" dest)
     (directory (<< "~{dest}/plugins/") :present)
     (chown dest uid gid)
     (clone "git://github.com/tmux-plugins/tpm" (<< "~{dest}/plugins/tpm"))
     (symlink (<< "~{dest}/.tmux.conf") (<< "~{home}/.tmux.conf") :present))))

(defn zsh
  "zsh setup"
  [{:keys [home user]}]
  (let [dest (<< "~{home}/.tmux")]
    (->
     (package "zsh")
     (contains "/etc/passwd" "re-ops:/bin/zsh")
     (unless "/usr/bin/chsh" "-s" "/usr/bin/zsh" user))))

(defn oh-my-zsh
  "Setup https://github.com/robbyrussell/oh-my-zsh"
  [{:keys [home uid gid]}]
  (let [dest (<< "~{home}/.oh-my-zsh")]
    (clone "git://github.com/narkisr/oh-my-zsh.git" dest)
    (symlink  (<< "~{home}/.zshrc") (<< "{dest}/.zshrc") :present)))
