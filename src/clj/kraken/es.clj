(ns kraken.es
  (:require
    [config.core :refer [env]]
    [clj-http.client :as client]
    [cheshire.core :as cheshire]))

(defn search-size
  []
  (:search-size env "10"))

(defn rando-seed
  []
  (System/currentTimeMillis))

(defn simple-search
  [s]
  (cheshire/generate-string
   {:query
    {:simple_query_string
     {:query s
      :fields ["SuccessMessage" "SuccessEffect" "FailureMessage" "FailureEffect" "Item"]}}}))

(defn random-search
  []
  (cheshire/generate-string
   {:query
    {:function_score
     {:functions [
                  {:random_score
                   {:seed (str (rando-seed))}}]}}}))

(defn process-hit
  [hit]
  (conj (select-keys hit [:_id]) (:_source hit)))

(defn ten-random
  [_]
  {:status 200
   :body (let [hits (-> (client/post (str (:es-url env) "/crypt/scrolls/_search?size=" (search-size))
                              {:accept :json
                               :content-type :json
                               :body (random-search)})
                 :body
                 (cheshire/parse-string true)
                 :hits
                 :hits)
        results (mapv #(process-hit %) hits)]
    results)
   }
  )

(defn my-search
  [{:keys [parameters]}]
  
   (let [hits (-> (client/post (str (:es-url env) "/crypt/scrolls/_search?size=10&from=0")
                               {:accept :json
                                :content-type :json
                                :body (simple-search parameters)})
                  :body
                  (cheshire/parse-string true)
                  :hits
                  :hits)
         results (mapv #(process-hit %) hits)]
     (if results
       {:status 200
        :body results}
       {:status 404
        :body {:error "Quest not found"}})))

(defn search
  ([q]
  (search q 0))
  ([q from]
   (let [hits (-> (client/post (str (:es-url env) "/crypt/scrolls/_search?size=10&from=" from)
                               {:accept :json
                                :content-type :json
                                :body (simple-search q)})
                  :body
                  (cheshire/parse-string true)
                  :hits
                  :hits)
         results (mapv #(process-hit %) hits)]
     results)))

(comment
  (rando-seed)
(ten-random)
(my-search "click")
(search "click")
  )
