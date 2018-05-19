(ns re-base.common
  "Common utility functions for re-base"
  (:require-macros
   [clojure.core.strint :refer (<<)]))

(defn dots
  [home]
  (<< "~{home}/.dots"))
