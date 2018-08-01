(ns re-base.rcp.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-base.rcp.vim]
   [re-base.rcp.shell]
   [re-base.rcp.docker]
   [re-base.rcp.desktop]
   [re-base.rcp.backup]
   [re-base.rcp.preqs]
   [re-base.rcp.reops]
   [re-base.rcp.security]
   [re-base.rcp.zfs]
   [re-conf.resources.pkg :as p :refer (initialize)]
   [re-conf.core :refer (invoke invoke-all report-n-exit assert-node-major-version)]
   [re-conf.resources.log :refer (info debug error)]))

(defn desktop
  [env]
  (report-n-exit
   (invoke-all env
               re-base.rcp.vim
               re-base.rcp.backup
               re-base.rcp.docker
               re-base.rcp.desktop
               re-base.rcp.security
               re-base.rcp.shell)))

(defn backup
  [env]
  (report-n-exit
   (invoke-all env
               re-base.rcp.backup
               re-base.rcp.shell)))

(defn server
  [env]
  (report-n-exit
   (invoke-all env
               re-base.rcp.vim
               re-base.rcp.zfs
               re-base.rcp.docker
               re-base.rcp.shell)))

(defn public
  [env]
  (report-n-exit
   (invoke-all env
               re-base.rcp.vim
               re-base.rcp.shell
               re-base.rcp.security)))

(defn run-profile [env profile]
  (fn [_]
    (case (keyword profile)
      :desktop (desktop env)
      :server (server env)
      :public (public env)
      :backup (backup env))))

(defn -main [e profile & args]
  (assert-node-major-version)
  (let [env (if e (cljs.reader/read-string (io/slurp e)) {})]
    (take! (initialize)
           (fn [r]
             (info "Provisioning machine using re-base!" ::main)
             (take!
              (invoke-all env re-base.rcp.reops re-base.rcp.preqs) (run-profile env profile))))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn"))
