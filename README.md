# http-tool

[![Clojars Project](https://img.shields.io/clojars/v/com.github.paytonward6/http-tool.svg)](https://clojars.org/com.github.paytonward6/http-tool)

Clojure HTTP client using only java.net

## Installation

### Leiningen/Boot

`[com.github.paytonward6/http-tool "0.1.1"]`

### Clojure CLI/deps.edn

`com.github.paytonward6/http-tool {:mvn/version "0.1.1"}`

### Gradle

`implementation("com.github.paytonward6:http-tool:0.1.1")`


## Usage

```clojure
(require '[http-tool.client :as http])

(def response (http/get-url "https://google.com"))

;; Returns a hash map of:
;;
;;  {:body "..."
;;   :url "https://google.com"
;;   :status 200
;;   :headers {:status 200 :content-type "text/html; charset=ISO-8859-1" ...}
;;   :version 1.1}

```
