(ns ap.pages.fourier
  (:use [jayq.core :only
    [$ add-class fade-in fade-out on remove-class]])
  (:require
    [ap.dom :as dom]
    [ap.html :as html]
    [ap.main :as main]
    [ap.util :as util]))

(declare
  cos-coef-series
  cosine-terms
  square-wave-series
  rms-series)

;;------------------------------------------------------------------------------
;; Constants
;;------------------------------------------------------------------------------

(def max-n-value 50)
(def PI (aget js/Math "PI"))

;;------------------------------------------------------------------------------
;; Chart Ids
;;------------------------------------------------------------------------------

(def main-chart-id (util/uuid))
(def cos-coef-chart-id (util/uuid))
(def sin-coef-chart-id (util/uuid))
(def rms-chart-id (util/uuid))

;;------------------------------------------------------------------------------
;; Atoms
;;------------------------------------------------------------------------------

(def n-value (atom 0))
(def x-value (atom nil))

(defn on-change-n-value [_ _ _ new-n]
  (dom/set-html "n-value" (str "n = " new-n))
  (.setData (aget js/window main-chart-id) (square-wave-series new-n))
  (.draw (aget js/window main-chart-id))
  (.setData (aget js/window cos-coef-chart-id) (cos-coef-series new-n))
  (.draw (aget js/window cos-coef-chart-id))
  (.setData (aget js/window rms-chart-id) (rms-series new-n))
  (.draw (aget js/window rms-chart-id))
  (dom/set-html "equationsContainer" (html/cosine-terms new-n)))

(add-watch n-value :_ on-change-n-value)

(defn on-change-x-value [_ _ _ new-x]
  (if new-x
    (dom/set-html "x-value" (str "x = " new-x))
    (dom/set-html "x-value" "x = nil")))

(add-watch x-value :_ on-change-x-value)

;;------------------------------------------------------------------------------
;; Slider
;;------------------------------------------------------------------------------

(defn slider->n [slider-value]
  (if (odd? slider-value)
    (/ (- slider-value 1) 2)
    (/ slider-value 2)))

(defn on-slide [_js-event js-ui]
  (swap! n-value #(slider->n (.-value js-ui))))

(defn init-slider []
  (util/slider "#slider" {
    :value @n-value
    :min 0
    :max (* max-n-value 2)
    :step 1
    :slide on-slide }))

;;------------------------------------------------------------------------------
;; Square Wave Series
;;------------------------------------------------------------------------------

(defn square-wave-reference-series [] {
  :color "orange"
  :data (aget js/window "square-wave-data" "reference-line")
  :hoverable false })

(defn square-wave-cos-series [n] {
  :color "blue"
  :data (aget js/window "square-wave-data" "cosine-lines" (str n))
  :lines {
    ;; hide the cosine line when n is even
    :show (not (even? n))
  }
  :hoverable false })

(defn square-wave-best-fit-series [n] {
  :color "red"
  :data (aget js/window "square-wave-data" "best-fit-lines" (str n))
  :hoverable false })

(defn square-wave-series [n]
  (clj->js [
    (square-wave-reference-series)
    (square-wave-cos-series n)
    (square-wave-best-fit-series n)]))

;;------------------------------------------------------------------------------
;; Cosine Coefficient
;;------------------------------------------------------------------------------

(defn cos-coef-series [n]
  (clj->js [
    {
      :color "#ccc"
      :lines {
        :show false
      }
      :bars {
        :show true
      }
      ; :yaxis {
      ;   :min 0
      ;   :max 0.5
      ; }
      :data (aget js/window "square-wave-data" "cos-coef")
    }
    {  
      :color "blue"
      :lines {
        :show false
      }
      :bars {
        :show true
      }
      :data (.slice (aget js/window "square-wave-data" "cos-coef")
              0 (.ceil js/Math (/ n 2)))
    }]))

;;------------------------------------------------------------------------------
;; RMS Series
;;------------------------------------------------------------------------------

(defn rms-series [n]
  (clj->js [
    {
      :color "#ccc"
      :lines {
        :show false
      }
      :bars {
        :show true
      }
      ; :yaxis {
      ;   :min 0
      ;   :max 0.5
      ; }
      :data (aget js/window "square-wave-data" "rms")
    }
    {  
      :color "blue"
      :lines {
        :show false
      }
      :bars {
        :show true
      }
      :data (.slice (aget js/window "square-wave-data" "rms") 0 n)
    }]))

;;------------------------------------------------------------------------------
;; Equations
;;------------------------------------------------------------------------------

; (defn cosine-terms [n]
;   (map
;     (fn [n1] )
;     (range 1 n 2)))
;   (str "sauce " n)
;   )

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn on-hover-main-chart [js-evt js-pos js-itm]
  (let [x1 (* (.-x js-pos) 100)
        x2 (/ (.round js/Math x1) 100)]
    (if (not= x2 @x-value)
      (reset! x-value x2))
    (if (or (> x2 PI) (< x2 (* -1 PI)))
      (reset! x-value nil))))

(defn mouseout-main-chart []
  (reset! x-value nil))

(defn add-events []
  ;; TODO: write me
  )

;;------------------------------------------------------------------------------
;; Charts
;;------------------------------------------------------------------------------

(def main-chart-options {
  :grid {
    :hoverable true
  }})

(defn init-charts []
  (aset js/window main-chart-id
    (util/chart "#mainChart"
      (square-wave-series @n-value)
      main-chart-options))
  (.bind (js/jQuery "#mainChart") "plothover" on-hover-main-chart)
  (on ($ "#mainChart") "mouseout" mouseout-main-chart)
  (aset js/window cos-coef-chart-id
    (util/chart "#cosCoefChart" (cos-coef-series @n-value) {}))
  (aset js/window rms-chart-id
    (util/chart "#rmsChart" (rms-series @n-value) {})))

;;------------------------------------------------------------------------------
;; Page Init
;;------------------------------------------------------------------------------

(defn init2 []
  (main/set-page-body (html/fourier))
  (init-slider)
  (init-charts)
  (add-events)
  (swap! n-value identity))

(defn init []
  (ap.data.load-square-wave-data init2))
