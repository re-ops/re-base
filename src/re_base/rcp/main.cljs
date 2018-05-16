(ns re-base.rcp.main
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-base.rcp.shell]
   [re-conf.cljs.pkg :as p :refer (initialize)]
   [re-conf.cljs.core :refer (invoke assert-node-major-version)]
   [re-conf.cljs.log :refer (info debug error)]))

(defn -main [e & args]
  (assert-node-major-version)
  (let [env (if e (cljs.reader/read-string (io/slurp f)) {})]
    (take! (initialize)
           (fn [r]
             (info "Started provisioning using re-base" ::main)
             (invoke re-base.rcp.shell env)))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn"))
