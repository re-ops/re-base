(ns re-base.rcp.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-base.rcp.shell]
   [re-conf.resources.pkg :as p :refer (initialize)]
   [re-conf.core :refer (invoke assert-node-major-version)]
   [re-conf.resources.log :refer (info debug error)]))

(defn home
  [{:keys [user] :as m}]
  (assoc m :home (<< "/home/~{user}")))

(defn -main [e & args]
  (assert-node-major-version)
  (let [env (if e (home (cljs.reader/read-string (io/slurp e))) {})]
    (take! (initialize)
           (fn [r]
             (info "Started provisioning using re-base" ::main)
             (invoke re-base.rcp.shell env)))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn"))
