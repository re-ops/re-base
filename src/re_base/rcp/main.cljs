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

(defn re-ops
  [env]
  (report-n-exit
   (invoke-all env re-base.rcp.reops)))

(defn with-preqs
  [profile env]
  (take!
   (invoke-all env re-base.rcp.preqs) (profile env)))

(defn run-profile [env profile]
  (fn [_]
    (case (keyword profile)
      :desktop (with-preqs desktop env)
      :server  (with-preqs server env)
      :public  (with-preqs public env)
      :backup  (with-preqs backup env)
      :re-ops  (re-ops env))))

(defn- home
  "Add home to the env"
  [{:keys [users] :as m}]
  (let [{:keys [name]} (users :main)]
    (assoc m :home (<< "/home/~{name}"))))

(defn- main-user
  "Add main user to env root"
  [{:keys [users] :as m}]
  (merge m (users :main)))

(defn enrich [env]
  (-> env home main-user))

(defn -main [e profile & args]
  (assert-node-major-version)
  (let [env (if e (enrich (cljs.reader/read-string (io/slurp e))) {})]
    (take! (initialize)
           (fn [r]
             (info "Provisioning machine using re-base!" ::main)
             (run-profile env profile)))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn"))
