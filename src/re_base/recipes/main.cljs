(ns re-base.recipes.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-base.recipes.nvim]
   [re-base.recipes.kvm]
   [re-base.recipes.langs]
   [re-base.recipes.shell]
   [re-base.recipes.build]
   [re-base.recipes.docker]
   [re-base.recipes.web]
   [re-base.recipes.xfce]
   [re-base.recipes.backup]
   [re-base.recipes.preqs]
   [re-base.recipes.reops]
   [re-base.recipes.security]
   [re-base.recipes.zfs]
   [re-base.recipes.networking]
   [re-conf.resources.pkg :as pkg]
   [re-conf.resources.firewall :as fire]
   [re-conf.cli :refer (parse-options into-categories)]
   [re-conf.core :refer (invoke invoke-all report-n-exit assert-node-major-version)]
   [re-conf.resources.log :as log :refer (info debug error)]))

(def category-m
  {:backup #{re-base.recipes.backup}
   :nas #{re-base.recipes.zfs}
   :virt #{re-base.recipes.docker re-base.recipes.kvm}
   :dev #{re-base.recipes.build re-base.recipes.langs}
   :base #{re-base.recipes.nvim re-base.recipes.shell re-base.recipes.security re-base.recipes.networking}
   :re-ops #{re-base.recipes.reops}
   :preqs #{re-base.recipes.preqs}
   :desktop #{re-base.recipes.xfce re-base.recipes.web}})

(defn run-categories [env cs]
  (let [namespaces (mapcat (fn [k] (category-m k)) (into-categories cs))]
    (take!
     (invoke-all env [re-base.recipes.preqs])
     (fn [_]
       (report-n-exit (invoke-all env namespaces))))))

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

(defn -main [& args]
  (assert-node-major-version)
  (let [{:keys [options] :as m} (parse-options args category-m)
        {:keys [environment categories]} options
        env (enrich (cljs.reader/read-string (io/slurp environment)))]
    (take! (async/merge [(pkg/initialize) (fire/initialize)])
           (fn [r]
             (info "Provisioning machine using re-base!" ::main)
             (run-categories env categories)))))

(set! *main-cli-fn* -main)

