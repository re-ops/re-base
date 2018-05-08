(ns re-base.rcp.main
  (:require
   [cljs.core.async :as async :refer [take!]]
   [re-base.rcp.shell]
   [re-conf.cljs.pkg :as p :refer (initialize)]
   [re-conf.cljs.core :refer (invoke assert-node-major-version)]
   [re-conf.cljs.log :refer (info debug error)]))

(defn -main [& args]
  (assert-node-major-version)
  (take! (initialize)
         (fn [r]
           (info "Started re-conf provisioning re-base" ::main)
           (invoke re-base.rcp.shell {:user "re-ops"}))))

(set! *main-cli-fn* -main)

(comment
  (-main))
