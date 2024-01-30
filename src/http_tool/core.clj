(ns http-tool.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:import (java.net.http HttpRequest HttpClient HttpClient$Version HttpClient$Redirect HttpHeaders HttpResponse HttpResponse$BodyHandlers)
           (java.net URI)))


(defn truthy-key [h1 h2]
  (map last (filter #((first %) h1) h2)))


(def funcs {:one '(.add 1) :two '(.add 2) :three '(.add 3)})

;;(defmacro do-pairs [obj vals]
;;  (let [to-call (truthy-key vals funcs)]
;;    `(doto ~obj
;;       ~@to-call)))

(defmacro do-list [obj calls]
     (concat `(doto ~obj) (eval calls)))

(defn ret-list [] '((.add 1) (.add 2)))

(do-list (java.util.ArrayList.) (truthy-key {:two true :one true} funcs))

(macroexpand-1 '(do-list (java.util.ArrayList.) (ret-list)))

(do-list (java.util.ArrayList.) (ret-list))
(do-list (java.util.ArrayList.) '((.add 1) (.add 2)))

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
   :version (let [version (.. resp version)
                  _ (println version)]
              (condp = version
                HttpClient$Version/HTTP_1_1 1.1
                HttpClient$Version/HTTP_2 2))})

