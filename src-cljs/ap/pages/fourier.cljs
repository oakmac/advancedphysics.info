(ns ap.pages.fourier
  (:use [jayq.core :only
    [$ add-class fade-in fade-out on remove-class]])
  (:require
    [ap.dom :as dom]
    [ap.html :as html]
    [ap.main :as main]
    [ap.util :as util]))

;;------------------------------------------------------------------------------
;; Charts
;;------------------------------------------------------------------------------

(defn init-charts []
  (util/chart "#chart1" [[[0 1] [1 2] [4 5]]] {})
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
  (add-events)
  (init-charts))