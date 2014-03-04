(ns ap.math.fourier
  (:require [ap.util :as util]))

;;------------------------------------------------------------------------------
;; Constants
;;------------------------------------------------------------------------------

(def PI (aget js/Math "PI"))
(def max-n-value 50)
(def x-points (map #(/ % 100) (range -314 316 2)))

;;------------------------------------------------------------------------------
;; Alias
;;------------------------------------------------------------------------------

(defn cos [x]
  (.cos js/Math x))

(defn sin [x]
  (.sin js/Math x))

(defn sqrt [x]
  (.sqrt js/Math x))

;;------------------------------------------------------------------------------
;; Util
;;------------------------------------------------------------------------------

(defn square [x]
  (* x x))

(defn mean [coll]
  (/
    (reduce + 0 coll)
    (count coll)))

;;------------------------------------------------------------------------------
;; Square Wave
;;------------------------------------------------------------------------------

(defn square-wave-reference-point [x]
  (if (or (< x (/ (* -1 PI) 2)) (> x (/ PI 2)))
    -1 1))

;; NOTE: should we just hard-code this?
; (def square-wave-reference-line [
;   [(* -1 PI) -1]
;   [(/ (* -1 PI) 2) -1]
;   [(/ (* -1 PI) 2) 1]
;   [(/ PI 2) 1]
;   [(/ PI 2) -1]
;   [PI -1]])

(defn square-wave-reference-line []
  (into []
    (map
      (fn [x] [x (square-wave-reference-point x)])
      x-points)))

(defn square-wave-cos-point [n x]
  (* (/ 4 (* n PI)) (cos (* n x))))

(defn square-wave-cos-line [n]
  (into []
    (map
      (fn [x] [x (square-wave-cos-point n x)])
      x-points)))

(defn square-wave-cos-lines []
  (reduce
    (fn [v n]
      (assoc v (str n) (square-wave-cos-line n)))
    {}
    (range 1 max-n-value 2)))

(defn square-wave-best-fit-point [n x]
  (reduce
    +
    0
    (map-indexed
      (fn [idx n1]
        (if (odd? idx)
          (* -1 (square-wave-cos-point n1 x))
          (square-wave-cos-point n1 x)))
      (range 1 (+ 1 n) 2))))

(defn square-wave-best-fit-line [n]
  (into []
    (map
      (fn [x] [x (square-wave-best-fit-point n x)])
      x-points)))

(defn square-wave-best-fit-lines []
  (reduce
    (fn [v n]
      (assoc v (str n) (square-wave-best-fit-line n)))
    {}
    (range 1 (+ 1 max-n-value) 1)))

(defn cos-coefficient [n]
  (/ 4 (* PI n)))

(defn square-wave-cos-coef-points []
  (into []
    (map-indexed
      (fn [idx n]
        [n (* (if (odd? idx) -1 1) (cos-coefficient n))])
      (range 1 (+ max-n-value) 2))))

(defn line-diff [[x1 y1] [x2 y2]]
  (- y1 y2))

(defn square-wave-rms-point [n]
  (let [reference-line (square-wave-reference-line)
        best-fit-points (square-wave-best-fit-line n)
        diff (map line-diff reference-line best-fit-points)
        diff-squared (map square diff)
        the-mean (mean diff-squared)]
    (sqrt the-mean)))

(defn square-wave-rms-points []
  (into []
    (map
      (fn [n] [n (square-wave-rms-point n)])
      (range 1 (+ 1 max-n-value) 1))))

(defn square-wave-page-data [] {
  :reference-line (square-wave-reference-line)
  :cosine-lines (square-wave-cos-lines)
  :best-fit-lines (square-wave-best-fit-lines)
  :cos-coef (square-wave-cos-coef-points)
  :rms (square-wave-rms-points)
  })

;;------------------------------------------------------------------------------
;; Sawtooth Wave
;;------------------------------------------------------------------------------

