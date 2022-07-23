(ns kraken.handler
  (:require
    [kraken.routes :refer [ping-routes random-route]]
    [reitit.coercion.schema]
    [reitit.ring :as reitit-ring]
    [reitit.ring.coercion :refer [coerce-exceptions-middleware                                                                                    
                                  coerce-request-middleware                                                                                       
                                  coerce-response-middleware]]       
    [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                            format-request-middleware
                                            format-response-middleware]]
    [muuntaja.core :as m]
    [reitit.ring.middleware.parameters :refer [parameters-middleware]]
    ;[ring.util.response :as r :refer [response]]
    [kraken.middleware :refer [middleware]]
    [kraken.es :refer [ten-random my-search]]
    [hiccup.page :refer [include-js include-css html5]]
    [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to kraken"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exciting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))


(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})
;; https://github.com/ring-clojure/ring/wiki/Static-Resources
;; this might be useful if combine balron.com 


(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [
     ["/" {:get {:handler index-handler}}]
      ["/api"
      ping-routes 
      random-route ]]
      {
         :data {:muuntaja m/instance
                :middleware [
                         parameters-middleware
                         format-negotiate-middleware
                         format-response-middleware
                         format-request-middleware
                         coerce-exceptions-middleware
                         coerce-request-middleware
                         coerce-response-middleware]}

       }
    )
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))
   {:middleware middleware}))


(comment 
  (app {:request-method :get
      :uri "/api/v1.0/search"})
(ten-random)
  )
