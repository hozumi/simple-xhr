# simple-xhr

A ClojureScript library designed to make a simple ajax request.

## Usage

```clojure
[simple-xhr "0.1.1"]
```

```clojure
(ns myapp
  (:require [simple-xhr :as sxhr]))

(sxhr/request
  "/api/messages" "/api/messages"
  :method "POST"
  :json {:title "hello" :body "world"}
  :complete
  (fn [xhrio]
    (let [content (-> xhrio
                      .getResponseJson
                      (js->clj :keywordize-keys true))]
      (when (.isSuccess xhrio)
        (.log js/console content)))))
```

## License

Distributed under the Eclipse Public License, the same as Clojure.
