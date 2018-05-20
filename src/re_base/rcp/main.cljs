(ns re-base.rcp.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-base.rcp.shell]
   [re-base.rcp.preqs]
   [re-base.rcp.backup]
   [re-conf.resources.pkg :as p :refer (initialize)]
   [re-conf.core :refer (invoke assert-node-major-version)]
   [re-conf.resources.log :refer (info debug error)]))

(defn -main [e & args]
  (assert-node-major-version)
  (let [env (if e (cljs.reader/read-string (io/slurp e)) {})]
    (take! (initialize)
           (fn [r]
             (info "Started provisioning using re-base" ::main)
             (invoke re-base.rcp.preq env)
             (invoke re-base.rcp.backup env)
             (invoke re-base.rcp.shell env)))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn"))
