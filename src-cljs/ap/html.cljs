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
;; Fourier Page
;;------------------------------------------------------------------------------

(hiccups/defhtml fourier []
  [:div#chart1.big-14ff7]
  [:div
    [:div#chart2.small-85a5a]
    [:div#chart3.small-85a5a]
    [:div#chart4.small-85a5a]
    [:div.clearfix-922e7]]
  [:div#slider.slider-dca20]
  [:div#slider-value]
  [:div#n-value])

;;------------------------------------------------------------------------------
;; Application Shell
;;------------------------------------------------------------------------------

(hiccups/defhtml init []
  [:div#bodyContainer])
