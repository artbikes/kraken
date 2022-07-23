(ns kraken.routes
  (:require [kraken.es :refer [ten-random]]))

(def ping-routes
  ["/ping" {:name :ping
            :get (fn [_]
                   {:status 200
                    :body {:ping "pong"}})}])

(def random-route

  ;;["/v1.0/search" {:get { :handler ten-random }} ]
  ["/v1.0/search" { :get ten-random}])


(comment

(ten-random)
(random-route)
  )
