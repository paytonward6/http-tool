(defproject com.github.paytonward6/http-tool "0.1.1"
  :description "Clojure HTTP client using only java.net"
  :url "https://github.com/paytonward6/http-tool"
  :license {:name "MIT"
            :url "https://mit-license.org/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.5.0"]
                 [org.clojure/core.async "1.6.673"]]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:require [[nrepl/nrepl "1.1"]
                             [cider/cider-nrepl "0.42.1"]]}})
