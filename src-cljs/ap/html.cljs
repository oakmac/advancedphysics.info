(ns ap.html
  (:require-macros
    [hiccups.core :as hiccups])
  (:require
    [hiccups.runtime :as hiccupsrt]
    [ap.util :as util]))

;;------------------------------------------------------------------------------
;; Util
;;------------------------------------------------------------------------------

(defn encode [a-string]
  (goog.string/htmlEscape a-string))

;;------------------------------------------------------------------------------
;; Equations
;;------------------------------------------------------------------------------

(defn negative-term? [n]
  (= (mod (+ n 1) 4) 0))

(hiccups/defhtml cosine-term [n]
  [:div.term-6729f
    (str
      (if (negative-term? n) "- " "+ ")
      "(4/" n "&pi;) cos(" n "x)")])

(hiccups/defhtml cosine-terms [n]
  (map
    #(if (odd? %) (cosine-term %) "")
    (range 1 (+ n 1)))
  [:div.clearfix-922e7])

;;------------------------------------------------------------------------------
;; Fourier Page
;;------------------------------------------------------------------------------

(hiccups/defhtml fourier []
  [:div.charts-55f2b
    [:div#mainChart.big-14ff7]
    [:div.right-739cf
      [:div#cosCoefChart.small-85a5a.push-3d1ce]
      [:div#sinCoefChart.small-85a5a.push-3d1ce]
      [:div#rmsChart.small-85a5a]]
    [:div.clearfix-922e7]]
  [:div#slider.slider-dca20]
  [:div.legend-f6c57
    [:div#n-value]
    [:div#x-value]]
  [:div#equationsContainer.math-8cbb7])

;;------------------------------------------------------------------------------
;; Application Shell
;;------------------------------------------------------------------------------

(hiccups/defhtml init []
  [:div#bodyContainer])
