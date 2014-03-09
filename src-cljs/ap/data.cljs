(ns ap.data
  (:require [ap.util :as util]))

;;------------------------------------------------------------------------------
;; Square Wave Data
;;------------------------------------------------------------------------------

;; TODO: this will be replaced with loading the data from a static JSON file
(defn load-square-wave-data [next]
  (aset js/window "square-wave-data" (ap.math.fourier.square-wave-page-data))
  (next))

;;------------------------------------------------------------------------------
;; Sawtooth
;;------------------------------------------------------------------------------
