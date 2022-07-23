(ns kraken.server
    (:require
     [kraken.handler :refer [app]]
     [config.core :refer [env]]
;;     [ring.adapter.jetty :refer [run-jetty]]
     [org.httpkit.server :refer [run-server]]
     )
    (:gen-class))

(defonce server (atom nil))

(defn -main []
  (println "Serer started")
  (reset! server (run-server app {:port 3000})))

(defn stop-server []
  (when-not (nil? @server)
           (@server :timeout 100)
           (reset! server nil)))

(defn restart-server []
  (stop-server)
  (-main))


(comment
  (restart-server))


