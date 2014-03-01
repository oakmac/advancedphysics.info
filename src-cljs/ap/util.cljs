(ns ap.util)

;;------------------------------------------------------------------------------
;; Logging
;;------------------------------------------------------------------------------

(defn log [thing]
  (.log js/console (pr-str thing)))

(defn js-log [thing]
  (.log js/console thing))

;;------------------------------------------------------------------------------
;; HTML Encode / Decode
;;------------------------------------------------------------------------------

(defn encode [thing]
  (goog.string/htmlEscape thing))

(defn un-encode [thing]
  (goog.string/unescapeEntities thing))

;;------------------------------------------------------------------------------
;; Random Int
;;------------------------------------------------------------------------------

(defn random-int [min max]
  (+ (.floor js/Math (* (.random js/Math) (- max (+ 1 min)))) min))

;;------------------------------------------------------------------------------
;; UUID
;;------------------------------------------------------------------------------

;; http://tinyurl.com/lz3bpg6
(defn uuid []
  (apply
   str
   (map
    (fn [x]
      (if (= x \0)
        (.toString (bit-or (* 16 (.random js/Math)) 0) 16)
        x))
    "00000000-0000-4000-0000-000000000000")))

;;------------------------------------------------------------------------------
;; Interface to localStorage
;;------------------------------------------------------------------------------

(defn localstorage-set [key value]
  (aset (.-localStorage js/window) key (pr-str value)))

(defn localstorage-get
  "returns the Clojure data structure stored at key; nil if the key does not
  exit or is invalid EDN"
  [key]
  (try
    (cljs.reader/read-string
      (aget (.-localStorage js/window) key))
    (catch js/Error e nil)))

;;------------------------------------------------------------------------------
;; Flot Chart
;;------------------------------------------------------------------------------

(defn chart [sel data cfg]
  (.plot js/jQuery sel (clj->js data) (clj->js cfg)))

;;------------------------------------------------------------------------------
;; jQuery-UI Slider
;;------------------------------------------------------------------------------

(defn slider [sel cfg]
  (.slider (js/jQuery sel) (clj->js cfg)))