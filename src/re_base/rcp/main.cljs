(ns re-base.rcp.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-base.rcp.shell]
   [re-base.rcp.docker]
   [re-base.rcp.desktop]
   [re-base.rcp.backup]
   [re-base.rcp.preqs]
   [re-conf.resources.pkg :as p :refer (initialize)]
   [re-conf.core :refer (invoke invoke-all report-n-exit assert-node-major-version)]
   [re-conf.resources.log :refer (info debug error)]))

(defn desktop
  [env]
  (report-n-exit
   (invoke-all env
               re-base.rcp.backup
               re-base.rcp.docker
               re-base.rcp.desktop
               re-base.rcp.shell)))

(defn backup
  [env]
  (report-n-exit
   (invoke-all env
               re-base.rcp.backup
               re-base.rcp.shell)))

(defn -main [e profile & args]
  (assert-node-major-version)
  (let [env (if e (cljs.reader/read-string (io/slurp e)) {})]
    (take! (initialize)
           (fn [r]
             (info "Provisioning machine using re-base!" ::main)
             (take! (invoke env re-base.rcp.preqs)
                    (fn [_]
                      (case profile
                        :desktop (desktop env)
                        :backup (backup env))))))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn"))
