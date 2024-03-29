(defproject com.github.paytonward6/http-tool "0.1.2"
  :description "Clojure HTTP client using only java.net"
  :url "https://github.com/paytonward6/http-tool"
  :license {:name "MIT"
            :url "https://mit-license.org/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.5.0"]
                 [org.clojure/core.async "1.6.673"]
                 [org.clojure/data.json "2.5.0"]]
  ;;:main "http-tool.core"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:dependencies [[nrepl "1.1.0"]
                                  [cider/cider-nrepl "0.45.0"]
                                  [org.clojure/tools.namespace "1.4.5"]]}
                   :aliases ^:replace {"repl" "repl"}})
