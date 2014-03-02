(ns ap.pages.fourier
  (:use [jayq.core :only
    [$ add-class fade-in fade-out on remove-class]])
  (:require
    [ap.dom :as dom]
    [ap.html :as html]
    [ap.main :as main]
    [ap.util :as util]))

(declare
  square-wave-series)

;;------------------------------------------------------------------------------
;; Constants
;;------------------------------------------------------------------------------

(def PI (aget js/Math "PI"))

;;------------------------------------------------------------------------------
;; Atoms
;;------------------------------------------------------------------------------

(def slider-value (atom 0))

(defn slider->n [slider-value]
  (if (odd? slider-value)
    (/ (- slider-value 1) 2)
    (/ slider-value 2)))

(defn on-change-slider [_ _ _ new-slider-value]
  (let [n (slider->n new-slider-value)]
    (dom/set-html "slider-value" (str "slider = " new-slider-value))
    (dom/set-html "n-value" (str "n = " n))
    (.setData (aget js/window "fourier-chart-1") (square-wave-series n))
    (.draw (aget js/window "fourier-chart-1"))
    )
  nil)

(add-watch slider-value :_ on-change-slider)

;;------------------------------------------------------------------------------
;; Slider
;;------------------------------------------------------------------------------

(defn on-slide [js-event js-ui]
  (reset! slider-value (.-value js-ui)))

(defn init-slider []
  (util/slider "#slider" {
    :value @slider-value
    :min 0
    :max 200
    :step 1
    :slide on-slide }))

;;------------------------------------------------------------------------------
;; Square Wave Series
;;------------------------------------------------------------------------------

(defn square-wave-cos-series [n]
  (let [z (/ n 100)]
  {
  :color "blue"
  :data [
    [(* -1 PI) (- -1.2 z)]
    [(/ (* -1 PI) 2) (+ -1.2 z)]
    [(/ (* -1 PI) 2) (+ 1.2 z)]
    [(/ PI 2) (+ 1.2 z)]
    [(/ PI 2) (+ -1.2 z)]
    [PI (- -1.2 z)]]}))

(defn square-wave-sin-series [n]
  (let [z (/ n 200)]
  {
  :color "yellow"
  :data [
    [(* -1 PI) (- -1.2 z)]
    [(/ (* -1 PI) 2) (+ -1.2 z)]
    [(/ (* -1 PI) 2) (+ 1.2 z)]
    [(/ PI 2) (+ 1.2 z)]
    [(/ PI 2) (+ -1.2 z)]
    [PI (- -1.2 z)]]}))

(def square-wave-reference-series {
  :color "orange"
  :data [
    [(* -1 PI) -1]
    [(/ (* -1 PI) 2) -1]
    [(/ (* -1 PI) 2) 1]
    [(/ PI 2) 1]
    [(/ PI 2) -1]
    [PI -1]]})

(defn square-wave-series [n]
  (clj->js [
    square-wave-reference-series
    (square-wave-cos-series n)
    (square-wave-sin-series n)]))

;;------------------------------------------------------------------------------
;; Charts
;;------------------------------------------------------------------------------

(defn init-charts []
  (aset js/window "fourier-chart-1"
    (util/chart "#chart1" (square-wave-series @slider-value) {}))
  ;(util/chart "#chart2" [[[0 1] [1 2] [4 5]]] {})
  ;(util/chart "#chart3" [[[0 1] [1 2] [4 5]]] {})
  ;(util/chart "#chart4" [[[0 1] [1 2] [4 5]]] {})
  )

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
  (add-events)
  (swap! slider-value identity))
