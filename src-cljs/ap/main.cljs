(ns ap.main
  (:use [jayq.core :only
    [$ add-class document-ready fade-in fade-out on parents prepend
     remove-class has-class]])
  (:require
    [ap.dom :as dom]
    [ap.html :as html]
    [ap.util :as util]))

(declare
  current-page
  set-hash)

;;------------------------------------------------------------------------------
;; Pages
;;------------------------------------------------------------------------------

(def pages {
  "waves-and-optics/fourier-analysis" {
    :name "Fourier Analysis"
    :init ap.pages.fourier.init
  }
})

;;------------------------------------------------------------------------------
;; Util
;;------------------------------------------------------------------------------

(defn set-page-body [html]
  (dom/set-html "bodyContainer" html))

(defn set-current-page [new-url]
  (if (and (not= new-url @current-page) (get pages new-url))
    (swap! current-page (fn [] new-url))))

;;------------------------------------------------------------------------------
;; Current Page Atom
;;------------------------------------------------------------------------------

(def current-page (atom nil))

(defn change-current-page [_ _ old-page-url new-page-url]
  (set-hash new-page-url)
  (apply (:init (get pages new-page-url)) []))

(add-watch current-page :_ change-current-page)

;;------------------------------------------------------------------------------
;; URL Hash
;;------------------------------------------------------------------------------

(defn set-hash [url]
  (aset (.-location js/window) "hash" (str "!" url)))

(defn get-hash []
  (let [url (.-hash (.-location js/window))]
    (.replace url #"^#!" "")))

(defn on-hash-changed []
  (let [hash1 (get-hash)]
    (cond
      ;; if it's a valid page and not the current page, switch to that page
      (and (get pages hash1) (not= hash1 @current-page))
        (set-current-page hash1)
      ;; if it's not a valid page, set the hash to the current page
      (nil? (get pages hash1))
        (set-hash @current-page)
      ;; else it's already the current-page, do nothing
      :else nil)))

;;------------------------------------------------------------------------------
;; Global Events
;;------------------------------------------------------------------------------

(defn click-page-link [e]
  (.preventDefault e)
  (let [target-el (.-currentTarget e)
        page-id (.getAttribute target-el "data-page-id")]
    (if (get pages page-id)
      (set-current-page page-id))))

(defn add-events []
  (aset js/window "onhashchange" on-hash-changed)
  (-> ($ :html)
    (on "click" ".link-c4a1e" click-page-link)))

;;------------------------------------------------------------------------------
;; Global App Init
;;------------------------------------------------------------------------------

(defn init []
  (prepend ($ "body") (html/init))
  (add-events)
  (set-current-page "waves-and-optics/fourier-analysis"))

(document-ready init)