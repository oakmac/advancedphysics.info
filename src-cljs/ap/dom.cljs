(ns ap.dom
  (:require
    [ap.util :as util]))

;;------------------------------------------------------------------------------
;; General
;;------------------------------------------------------------------------------

(defn by-id [el-or-id]
  (if (= (type el-or-id) js/String)
    (.getElementById js/document el-or-id)
    el-or-id))

(defn set-html [el html]
  (aset (by-id el) "innerHTML" html))

(defn show-el [el]
  (aset (by-id el) "style" "display" ""))

(defn hide-el [el]
  (aset (by-id el) "style" "display" "none"))
