(ns apns-test.core
  (:require [herolabs.apns.ssl  :as ssl ]
            [clojure.java.io    :as io  ])
  (:import [io.netty.channel.nio NioEventLoopGroup])
  (:use herolabs.apns.feedback))

(defn -main []
  (let [store (ssl/keystore
                :key-path (io/resource "my-project.p12")
                :cert-path (io/resource "my-project.cer"))
        tm    (ssl/naive-trust-managers
                :trace true)
        ctx   (ssl/ssl-context
                :trust-managers tm
                :keystore       store)
        el    (NioEventLoopGroup.)]
    (println "Fetching feedback...")
    (doseq [[token timestamp] (feedback (dev-address) ctx :event-loop el)]
      (println timestamp ":" token))
    (println "Terminating...")
    (.shutdown el)))
