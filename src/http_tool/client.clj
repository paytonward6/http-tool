(ns http-tool.client
  (:require [http-tool.core :as core]))

(defn get-url
  ([^String url] (get-url url nil))
  ([^String url opts]
   (let [resp (-> (core/new-client)
                  (core/send-req (core/req url) opts))]
     (core/resp-to-hashmap resp))))
