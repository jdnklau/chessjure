(defproject chessjure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot chessjure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
