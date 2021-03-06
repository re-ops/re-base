(ns re-base.recipes.preqs
  "Resource prerequisites"
  (:refer-clojure :exclude [update remove])
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.facts :refer (arm?)]
   [re-conf.resources.pkg :refer (package repository key-server update)]
   [re-conf.resources.file :refer (template directory)]
   [re-conf.resources.output :refer (summary)]))

(defn git
  "git setup"
  [{:keys [home git]}]
  (let [dest (<< "~{home}/.gitconfig")]
    (->
     (package "git")
     (directory home :present)
     (template "resources/git/gitconfig.mustache" dest git)
     (summary "git setup"))))

(defn apt-support []
  (->
   (package "dirmngr")
   (package "software-properties-common")
   (summary "apt utils support done")))

(defn barbecue
  "Setting up barbecue repo"
  []
  (when-not (arm?)
    (->
     (key-server "keyserver.ubuntu.com" "42ED3C30B8C9F76BC85AC1EC8B095396E29035F0")
     (repository "deb https://raw.githubusercontent.com/narkisr/fpm-barbecue/repo/packages/ubuntu/ xenial main" :present)
     (update)
     (summary "barbecue setup"))))
