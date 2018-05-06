(defproject re-base "0.1.0"
  :description "Base recipes"
  :url "https://github.com/re-ops/re-base"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]

                 ; clojurescript
                 [org.clojure/clojurescript "1.10.238"]

                 ; << macro
                 [org.clojure/core.incubator "0.1.4"]

                 [re-conf "0.1.0"]

                 ]
  :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-figwheel "0.5.14"]
            [lein-cljfmt "0.5.7"]
            ]


  :source-paths ["src"]

  :clean-targets ["server.js" "target"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :figwheel true
              :compiler {
                :main re-base.main
                :asset-path "target/js/compiled/dev"
                :output-to "target/js/compiled/re-base.js"
                :output-dir "target/js/compiled/dev"
                :target :nodejs
                :optimizations :none
                :source-map-timestamp true
		   }
             }
             {:id "test"
              :source-paths ["src" "test"]
              :notify-command ["node" "target/unit-tests.js"]
              :compiler {
                 :output-to "target/unit-tests.js"
                 :optimizations :none
                 :target :nodejs
               :main re-base.test.suite
		   }
		 }
             {:id "prod"
              :source-paths ["src"]
              :compiler {
                :output-to "re-base.js"
                :output-dir "target/js/compiled/prod"
                :target :nodejs
                :optimizations :simple}}]}

    :profiles {
      :dev {
         :dependencies
            [[figwheel-sidecar "0.5.14"] [com.cemerick/piggieback "0.2.2"]]
          :source-paths ["src" "dev"]
          :repl-options {
            :port 38081
            :init (do (fig-start))
            :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
           }
       }
    }

    :aliases {
       "travis" [
         "do" "clean," "cljfmt" "check," "npm" "install," "cljsbuild" "once" "prod," "cljsbuild" "test"
       ]
       
    }
)
