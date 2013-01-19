(ns ^{:doc "Make network requests.

  Adapted from ClojureScript One and Bobby Calderwood's Trail framework:
  https://github.com/brentonashworth/one/blob/master/src/lib/cljs/one/browser/remote.cljs
  https://github.com/bobby/trail"}
  simple-xhr
  (:require [goog.structs.Map :as goog.structs.Map]
            [goog.json :as gjson]
            [goog.net.XhrManager :as manager]))

(def xhr-manager
  (atom (goog.net.XhrManager. js/undefined
                              js/undefined
                              js/undefined
                              js/undefined
                              js/undefined)))

(defn request
  "Asynchronously make a network request for the resource at url. If
  provided via the `:on-success` and `:on-error` keyword arguments, the
  appropriate one of `on-success` or `on-error` will be called on
  completion. They will be passed a map containing the keys `:id`,
  `:body`, `:status`, and `:event`. The entry for `:event` contains an
  instance of the `goog.net.XhrManager.Event`.

  Other allowable keyword arguments are `:method`, `:content`, `:headers`,
  `:priority`, and `:retries`. `:method` defaults to \"GET\" and `:retries`
  defaults to `0`."
  [& {:keys [url id method content json headers priority retries
             complete success error complete-after]
      :or   {method   "GET"
             retries  0}}]
  (let [headers (if json (assoc headers "Content-Type" "application/json")
                    headers)
        content (if json
                  (-> json clj->js gjson/serialize) content)]
    (.send @xhr-manager
           (or id url)
           url
           method
           content
           (and headers (goog.structs.Map. (clj->js headers)))
           priority
           (fn [e] (let [xhrio (.-target e)]
                     (when complete
                       (complete xhrio))
                     (if (.isSuccess xhrio)
                       (when success
                         (success xhrio))
                       (when error
                         (error xhrio)))
                     (when complete-after
                       (complete-after xhrio))))
           retries)))
