(ns ap.main
  (:use [jayq.core :only
    [$ add-class document-ready fade-in fade-out on parents prepend
     remove-class has-class]])
  (:require
    [ap.dom :as dom]
    [ap.html :as html]
    [ap.util :as util]))

(declare
  current-page)

;;------------------------------------------------------------------------------
;; Pages
;;------------------------------------------------------------------------------

(def pages {
  :home {
    :name "Home"
    :init #(util/log "init homepage")
  }
})

;;------------------------------------------------------------------------------
;; Util
;;------------------------------------------------------------------------------

(defn set-page-body [html]
  (dom/set-html "bodyContainer" html))

(defn set-current-page [page]
  (if (not= (keyword page) @current-page)
    (swap! current-page #(keyword page))))

(defn set-hash [page-key]
  (aset (.-location js/window) "hash" (str "/" (name page-key))))

;;------------------------------------------------------------------------------
;; Current Page Atom
;;------------------------------------------------------------------------------

(def page-fade-speed-ms 120)

(def current-page (atom nil))

(defn show-new-page [new-page-key]
  (let [page (get pages new-page-key)
        init-fn (:init page)]
    (set-page-body "")
    (if init-fn
      (init-fn))
    (fade-in ($ "#bodyContainer") page-fade-speed-ms)))

(defn change-current-page
  [_ _ old-page-key new-page-key]
  (set-hash new-page-key)
  (if (and (:fade-page-transitions @y1.config.config) old-page-key)
    (fade-out ($ "#bodyContainer")
      page-fade-speed-ms
      #(show-new-page new-page-key))
    (apply (:init (get pages new-page-key)) [])))

(add-watch current-page :current-page change-current-page)

;;------------------------------------------------------------------------------
;; Global Events
;;------------------------------------------------------------------------------

(defn get-hash-as-page-key []
  (let [hash1 (.-hash (.-location js/window))
        hash2 (.replace hash1 #"^#/" "")]
    (keyword hash2)))

(defn on-hash-changed []
  (let [hash1 (get-hash-as-page-key)]
    (cond
      ;; if it's a valid page keyword and not the current page, switch
      ;; to that page
      (and (get pages hash1) (not= hash1 @current-page))
        (set-current-page hash1)
      ;; if it's not a valid page keyword, set the hash to the current page
      (nil? (get pages hash1))
        (set-hash @current-page)
      ;; else it's already the current-page, do nothing
      :else nil)))

(defn click-page-link [e]
  (.preventDefault e)
  (let [target-el (.-currentTarget e)
        page-id (.getAttribute target-el "data-page-id")]
    (if (get pages (keyword page-id))
      (set-current-page page-id))))

(defn add-events []
  (aset js/window "onhashchange" on-hash-changed)
  (-> ($ :html)
    (on "click" ".link-c4a1e" click-page-link)))

;;------------------------------------------------------------------------------
;; Global App Init
;;------------------------------------------------------------------------------

(defn init []
  (add-events)
  ;; TODO: yee-haw!
  )

(document-ready init)