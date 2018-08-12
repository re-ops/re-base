(ns re-base.recipes.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-base.recipes.vim]
   [re-base.recipes.shell]
   [re-base.recipes.docker]
   [re-base.recipes.desktop]
   [re-base.recipes.backup]
   [re-base.recipes.preqs]
   [re-base.recipes.reops]
   [re-base.recipes.security]
   [re-base.recipes.zfs]
   [re-conf.resources.pkg :as p :refer (initialize)]
   [re-conf.core :refer (invoke invoke-all report-n-exit assert-node-major-version)]
   [re-conf.resources.log :refer (info debug error)]))

(defn desktop
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.vim
               re-base.recipes.backup
               re-base.recipes.docker
               re-base.recipes.desktop
               re-base.recipes.security
               re-base.recipes.shell)))

(defn backup
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.backup
               re-base.recipes.shell)))

(defn server
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.vim
               re-base.recipes.zfs
               re-base.recipes.docker
               re-base.recipes.shell)))

(defn public
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.vim
               re-base.recipes.shell
               re-base.recipes.security)))

(defn re-ops
  [env]
  (report-n-exit
   (invoke-all env re-base.recipes.reops)))

(defn with-preqs
  [profile env]
  (take!
   (invoke-all env re-base.recipes.preqs) (fn [_] (profile env))))

(defn run-profile [env profile]
  (case (keyword profile)
    :desktop (with-preqs desktop env)
    :server  (with-preqs server env)
    :public  (with-preqs public env)
    :backup  (with-preqs backup env)
    :re-ops  (re-ops env)))

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
