(ns ap.data
  (:require [ap.util :as util]))

;;------------------------------------------------------------------------------
;; Square Wave Data
;;------------------------------------------------------------------------------

;; TODO: ugh - this is a mess
;; needs a re-write
(defn load-data [ls-key create-fn next]
  (let [d1 (util/localstorage-get ls-key)]
    (if d1
      (aset js/window ls-key d1)
      (let [d2 (clj->js (create-fn))]
        ;(util/localstorage-set ls-key d2)
        (aset js/window ls-key d2))))
  (next))

(defn load-square-wave-data [next]
  (load-data "square-wave-data" ap.math.fourier.square-wave-page-data next))

;;------------------------------------------------------------------------------
;; Sawtooth
;;------------------------------------------------------------------------------
