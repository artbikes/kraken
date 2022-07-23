(ns kraken.app
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn initial-inputs
  []
  {:quest ""})

(def app-state (atom {:inputs (initial-inputs)
                      :answers {} }))

(defn search
  []
  (get-in @app-state [:inputs :quest]))

(defn update-search
  [s]
  (swap! app-state assoc-in [:inputs :quest] s))

;; okay, so I see how updating the atom works
;;(update-search "bleagh")
;;(search)
