(ns kraken.pages.play
  (:require
    [reagent.core :as r :refer [atom]]
    [kraken.app :as a :refer [app-state]]
    ))


(defn reset-state
  []
  ;;(a/reset-index)
  ;;(a/reset-questions)
  ;; (a/next-question)
  )

 (defn quest-input []
    (let [val (r/cursor app-state [:inputs :quest])]
      (fn []
        [:div.input-wrapper
         [:label "Quest"]
         [:input {:type "text"
                  :value @val
                  :on-change #(reset! val (.. % -target -value))}]])))

  (defn submit-button []
    [:div.actions
     [:button {:type "submit"} "Submit"]])
   
  (defn submit-form [app-state]
    (let [{:keys [quest]} (:inputs app-state)]
      (-> app-state
          (assoc-in [:answers (rand)] quest)
      (assoc :inputs (a/initial-inputs)))))

  (defn form []
    [:form.input-form {:on-submit (fn [e]
                                    (.preventDefault e)
                                    (swap! app-state submit-form))}
     [quest-input]
     [submit-button]])

(defn answer
  []
  [:div#answer.flex-container
   [:div.text.flex-container.major-container
    [:p.flex-item.major-item (a/search)]]
   [:div.misc.flex-container.minor-container]]
  )
;;(a/update-search "migraine")
