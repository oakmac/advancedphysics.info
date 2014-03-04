(ns ap.data
  (:require [ap.util :as util]))

;;------------------------------------------------------------------------------
;; Square Wave Data
;;------------------------------------------------------------------------------

;; TODO: rewrite this... it's a mess
(defn load-square-wave-data [next]
  (let [stored-data (util/localstorage-get "square-wave-data")]
    (if stored-data
      (aset js/window "square-wave-data" stored-data)
      (let [the-data (ap.math.fourier.square-wave-page-data)]
        (util/localstorage-set "square-wave-data" the-data)
        (aset js/window "square-wave-data" the-data))))
  (next))

;;------------------------------------------------------------------------------
;; Sawtooth
;;------------------------------------------------------------------------------
