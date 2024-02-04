(ns http-tool.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:import (java.net.http HttpRequest HttpClient HttpClient$Version HttpClient$Redirect HttpHeaders HttpResponse HttpResponse$BodyHandlers)
           (java.net URI)))


(defn http-enum->version [v]
  (condp = v
    HttpClient$Version/HTTP_1_1 1.1
    HttpClient$Version/HTTP_2 2))

(defn version->http-enum [v]
  (cond
    (= 1.1 v) HttpClient$Version/HTTP_1_1
    (or (= 2 v) (= 2.0 v)) HttpClient$Version/HTTP_2))

(defn add-headers [req-builder headers]
  (doseq [[k v] headers]
    (.header req-builder (name k) (str v)))
  req-builder)

;;(defn add-query [req-builder headers])

(defn build-req [url & {:keys [version headers]}]
  (let [c (doto (HttpRequest/newBuilder)
            (.uri (URI. url))
            (.GET))]
    (cond-> c
      version (.version (version->http-enum version))
      headers (add-headers headers))
    (.build c)))

;;(.headers (build-req "https://google.com" :headers {:authorization "this" :this__n 2} :version 1.1))

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
    ^clojure.lang.PersistentHashMap [& {:keys [async] :as opts}]]
   (let [_ (println opts " :async" async)
         response (if async
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
   :version (http-enum->version (.. resp version))})

