(ns re-base.rcp.preqs
  "Resource prerequisites"
  (:require-macros
    [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.file :refer (template)]
   [re-conf.resources.output :refer (summary)]))

(defn git
  "git setup"
  [{:keys [home git]}]
  (let [dest (<< "~{home}/.gitconfig")]
    (println dest)
    (->
     (package "git")
     (template git "resources/gitconfig.mustache" dest )
     (summary "git setup"))))
