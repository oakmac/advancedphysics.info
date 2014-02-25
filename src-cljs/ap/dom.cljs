(ns ap.dom
  (:require
    [ap.util :as util]))

;;------------------------------------------------------------------------------
;; General
;;------------------------------------------------------------------------------

(defn by-id
  "Returns a DOM element."
  [el-or-id]
  (if (= (type el-or-id) js/String)
    (.getElementById js/document el-or-id)
    el-or-id))

(defn set-html [el html]
  (aset (by-id el) "innerHTML" html))

(defn show-el [el]
  (aset (by-id el) "style" "display" ""))

(defn hide-el [el]
  (aset (by-id el) "style" "display" "none"))

(defn make-vis [el]
  (aset (by-id el) "style" "visibility" "visible"))

(defn make-invis [el]
  (aset (by-id el) "style" "visibility" "hidden"))

(defn remove-el [el]
  (let [el1 (by-id el)
        parent-el (aget el1 "parentNode")]
    (.removeChild parent-el el1)))

(defn showing?
  "returns whether an element is showing or not
   NOTE: only checking if display:none"
  [el]
  (let [display (aget (by-id el) "style" "display")]
    (not (= display "none"))))
