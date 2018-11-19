(ns re-base.recipes.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-conf.cli :refer (parse-options)]
   [re-base.recipes.vim]
   [re-base.recipes.kvm]
   [re-base.recipes.shell]
   [re-base.recipes.build]
   [re-base.recipes.docker]
   [re-base.recipes.desktop]
   [re-base.recipes.backup]
   [re-base.recipes.preqs]
   [re-base.recipes.reops]
   [re-base.recipes.security]
   [re-base.recipes.zfs]
   [re-conf.resources.pkg :as pkg]
   [re-conf.resources.firewall :as fire]
   [re-conf.core :refer (invoke invoke-all report-n-exit assert-node-major-version)]
   [re-conf.resources.log :as log :refer (info debug error)]))

(defn desktop
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.vim
               re-base.recipes.backup
               re-base.recipes.build
               re-base.recipes.kvm
               re-base.recipes.docker
               re-base.recipes.desktop
               re-base.recipes.security
               re-base.recipes.shell)))

(defn backup
  "A machine running backup utilities"
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.backup
               re-base.recipes.shell)))

(defn hypervisor
  "A physical hypervisor server"
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.vim
               re-base.recipes.zfs
               re-base.recipes.kvm
               re-base.recipes.docker
               re-base.recipes.shell)))

(defn develop
  "A remove server used for development"
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.vim
               re-base.recipes.shell
               re-base.recipes.docker
               re-base.recipes.build
               re-base.recipes.security)))

(defn public
  "A web facing server"
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.vim
               re-base.recipes.shell
               re-base.recipes.docker
               re-base.recipes.security)))

(defn re-ops
  "Minimal configuration for supporting re-ops"
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
    :hypervisor (with-preqs hypervisor env)
    :public  (with-preqs public env)
    :develop  (with-preqs develop env)
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

(def profiles
  #{:desktop :server :public :backup :re-ops})

(defn -main [& args]
  (assert-node-major-version)
  (let [{:keys [options] :as m} (parse-options args profiles)
        {:keys [environment profile]} options
        env (enrich (cljs.reader/read-string (io/slurp environment)))]
    (take! (async/merge [(pkg/initialize) (fire/initialize)])
           (fn [r]
             (info "Provisioning machine using re-base!" ::main)
             (run-profile env profile)))))

(set! *main-cli-fn* -main)

