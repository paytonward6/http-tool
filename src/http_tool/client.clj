(ns http-tool.client
  (:require [http-tool.core :as core]
            [clojure.data.json :as json]))

(defn get-url [^String url & opts]
   (let [resp (-> (core/new-client)
                  (core/send-req (core/req url) opts))
         resp-hash (core/resp-to-hashmap resp)
         hash-opts (apply hash-map opts)]
     (if (:json hash-opts)
      (update resp-hash :body json/read-str :key-fn keyword)
       resp-hash)))

;; (get-url "http://127.0.0.1:8080/api/get")
