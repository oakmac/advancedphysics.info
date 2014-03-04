(ns ap.pages.fourier
  (:use [jayq.core :only
    [$ add-class fade-in fade-out on remove-class]])
  (:require
    [ap.dom :as dom]
    [ap.html :as html]
    [ap.main :as main]
    [ap.util :as util]))

(declare
  square-wave-series
  rms-series)

;;------------------------------------------------------------------------------
;; Constants
;;------------------------------------------------------------------------------

(def max-n-value 50)

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
    (.setData (aget js/window "main-fourier-chart") (square-wave-series n))
    (.draw (aget js/window "main-fourier-chart"))
    (.setData (aget js/window "rms-fourier-chart") (rms-series n))
    (.draw (aget js/window "rms-fourier-chart"))
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
    :max (* max-n-value 2)
    :step 1
    :slide on-slide }))

;;------------------------------------------------------------------------------
;; Square Wave Series
;;------------------------------------------------------------------------------

(defn square-wave-reference-series [] {
  :color "orange"
  :data (aget js/window "square-wave-data" "reference-line")})

(defn square-wave-cos-series [n] {
  :color "blue"
  :data (aget js/window "square-wave-data" "cosine-lines" (str n))
  :lines {
    ;; hide the cosine line when n is even
    :show (not (even? n)) }})

(defn square-wave-best-fit-series [n] {
  :color "red"
  :data (aget js/window "square-wave-data" "best-fit-lines" (str n))})

(defn square-wave-series [n]
  (clj->js [
    (square-wave-reference-series)
    (square-wave-cos-series n)
    (square-wave-best-fit-series n)]))

;;------------------------------------------------------------------------------
;; RMS Series
;;------------------------------------------------------------------------------

(defn rms-series [n]
  (clj->js [{
    :color "blue"
    :lines {
      :show false
    }
    :bars {
      :show true
    }
    :yaxis {
      :min 0
      :max 0.5
    }
    :data (aget js/window "square-wave-data" "rms") }]))

;;------------------------------------------------------------------------------
;; Charts
;;------------------------------------------------------------------------------

(defn init-charts []
  (aset js/window "main-fourier-chart"
    (util/chart "#mainChart" (square-wave-series @slider-value) {}))
  (aset js/window "rms-fourier-chart"
    (util/chart "#rmsChart" (rms-series @slider-value) {}))
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

(defn init2 []
  (main/set-page-body (html/fourier))
  (init-charts)
  (init-slider)
  (add-events)
  (swap! slider-value identity))

(defn init []
  (ap.data.load-square-wave-data init2))
