(ns ap.pages.fourier
  (:use [jayq.core :only
    [$ add-class fade-in fade-out on remove-class]])
  (:require
    [ap.dom :as dom]
    [ap.html :as html]
    [ap.main :as main]
    [ap.util :as util]))

(declare
  create-data-series)

;;------------------------------------------------------------------------------
;; Atoms
;;------------------------------------------------------------------------------

(def x-value (atom 1))

(defn on-change-x-value [_ _ _ new-x]
  (.setData (aget js/window "fourier-chart-1") (create-data-series new-x))
  (.draw (aget js/window "fourier-chart-1"))
  nil
  )

(add-watch x-value :_ on-change-x-value)

;;------------------------------------------------------------------------------
;; Slider
;;------------------------------------------------------------------------------

(defn on-slide [js-event js-ui]
  (reset! x-value (.-value js-ui)))

(defn init-slider []
  (util/slider "#slider" {
    :value @x-value
    :min 0
    :max 10
    :step 1
    :slide on-slide }))

;;------------------------------------------------------------------------------
;; Charts
;;------------------------------------------------------------------------------

(defn create-data-series [x]
  (clj->js [[[0 (* x 1)]
    [1 (* x 1.2)]
    [2 (* x 1.8)]
    [3 (* x 2)]
    [4 (* x 2.1)]
    [5 (* x 3)]]]))

(defn init-charts []
  (aset js/window "fourier-chart-1"
    (util/chart "#chart1" (create-data-series @x-value) {
      :yaxis {
        :min -5
        :max 50
      }
      }))
  (util/chart "#chart2" [[[0 1] [1 2] [4 5]]] {})
  (util/chart "#chart3" [[[0 1] [1 2] [4 5]]] {})
  (util/chart "#chart4" [[[0 1] [1 2] [4 5]]] {}))

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn add-events []
  ;; TODO: write me
  )

;;------------------------------------------------------------------------------
;; Page Init
;;------------------------------------------------------------------------------

(defn init []
  (main/set-page-body (html/fourier))
  (init-charts)
  (init-slider)
  (add-events))
