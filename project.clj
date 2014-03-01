(defproject ap "0.0.1"
  :description "advancedphysics.info"
  :source-paths ["src-clj"]

  :dependencies [
    [org.clojure/clojure "1.5.1"]
    [org.clojure/clojurescript "0.0-2127"
      :exclusions [org.apache.ant/ant]]
    [jayq "2.5.0"]
    [hiccups "0.3.0"]]

  :plugins [
    [lein-cljsbuild "1.0.1-SNAPSHOT"]]

  :cljsbuild {
    :builds [{
      :source-paths ["src-cljs"]
      :compiler {
        :output-to "resources/public/js/main.js"
        :optimizations :advanced
        :pretty-print true
        :externs [
          "externs/jquery-1.9.js"
          "externs/flot-0.8.2.js"
          "externs/jquery-ui-1.10.4.js"]}}]})