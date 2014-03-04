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

(defn localstorage-set-clj [k v]
  (aset (.-localStorage js/window) k (pr-str v)))

(defn localstorage-get-clj
  "returns the Clojure data structure stored at key; nil if the key does not
  exit or is invalid EDN"
  [k]
  (try
    (cljs.reader/read-string
      (aget (.-localStorage js/window) k))
    (catch js/Error e nil)))

(defn localstorage-set [k v]
  (aset (.-localStorage js/window) k (.stringify js/JSON (clj->js v))))

(defn localstorage-get [k]
  (try
    (.parse js/JSON (aget (.-localStorage js/window) k))
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