(ns re-base.rcp.preqs
  "Resource prerequisites"
  (:refer-clojure :exclude [update key remove])
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package repository key update)]
   [re-conf.resources.file :refer (template)]
   [re-conf.resources.output :refer (summary)]))

(defn git
  "git setup"
  [{:keys [home git]}]
  (let [dest (<< "~{home}/.gitconfig")]
    (->
     (package "git")
     (template git "resources/gitconfig.mustache" dest)
     (summary "git setup"))))

(defn barbecue
  "Setting up barbecue repo"
  []
  (->
   (repository "https://raw.githubusercontent.com/narkisr/fpm-barbecue/repo/packages/ubuntu/" :present)
   (key "keyserver.ubuntu.com" "42ED3C30B8C9F76BC85AC1EC8B095396E29035F0")
   (update)
   (summary "barbecue setup")))
