(ns re-base.rcp.shell
  "Shell setup recipes"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.cljs.pkg :refer (package)]
   [re-conf.cljs.file :refer (chown directory symlink)]
   [re-conf.cljs.facts :refer (home)]
   [re-conf.cljs.git :refer (clone)]
   [re-conf.cljs.shell :refer (exec)]
   [re-conf.cljs.output :refer (summary)]))

(defn tmux
  "Setup tmux for user"
  [{:keys [user gid uid]}]
  (let [home (<< "/home/~{user}") dest (<< "~{home}/.tmux")]
    (->
     (package "tmux")
     (clone "git://github.com/narkisr/.tmux.git" dest)
     (directory (<< "~{dest}/plugins/") :present)
     (chown dest uid gid)
     (clone "git://github.com/tmux-plugins/tpm" (<< "~{dest}/plugins/tpm"))
     (symlink  (<< "~{home}/.tmux.conf") (<< "{dest}/.tmux.conf") :present))))

(comment)
