(ns http-tool.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:import (java.net.http HttpRequest HttpClient HttpClient$Version HttpClient$Redirect HttpHeaders HttpResponse HttpResponse$BodyHandlers)
           (java.net URI)))


(defn req ^HttpRequest [^String url]
  (-> (HttpRequest/newBuilder)
      (.uri (URI. url))
      (.GET)
      (.build)))

(defn new-client ^HttpClient []
  (-> (HttpClient/newBuilder)
      (.followRedirects (HttpClient$Redirect/ALWAYS))
      (.build)))

(defn send-req
  (^HttpResponse [^HttpClient c ^HttpRequest req] (send-req c req {}))
  ([^HttpClient client
    ^HttpRequest req
    ^clojure.lang.PersistentHashMap {async :async}]
   (let [response (if async
                    (-> client
                        (.sendAsync req (HttpResponse$BodyHandlers/ofString))
                        (.get))
                    (-> client
                        (.send req (HttpResponse$BodyHandlers/ofString))))]
     response)))

(defn to-keyword [string]
  (if (str/starts-with? string ":")
    (keyword (apply str (rest string)))
    (keyword string)))

(defn parse-headers [^HttpHeaders headers]
  (zipmap (map to-keyword (.keySet headers)) (map first (.values headers))))

(defn resp-to-hashmap
  ^clojure.lang.PersistentHashMap [^HttpResponse resp]
  {:body (. resp body)
   :url (.. resp uri toString)
   :status (. resp statusCode)
   :headers (parse-headers (.. resp headers map))
   :version (let [version (.. resp version)
                  _ (println version)]
              (condp = version
                HttpClient$Version/HTTP_1_1 1.1
                HttpClient$Version/HTTP_2 2))})

