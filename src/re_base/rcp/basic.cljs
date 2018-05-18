(ns re-base.rcp.basic
  "Basic setup"
  (:require
   [re-conf.resources.download :refer (download checksum)]
   [re-conf.resources.archive :refer (unzip)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.output :refer (summary)]))

