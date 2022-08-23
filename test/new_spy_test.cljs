(ns new-spy-test
  (:require [clojure.test :refer [deftest testing is run-tests]]
            [email-sender :refer [send-message email-beatle]]))



(defn spy
  "If no function is supplied, returns a function that takes any number of args
  and returns nil.
  If a function is supplied, returns a function that wraps the function.
  Adds metadata {:calls (atom []) :responses (atom [])} to the function, calls
  and responses are recorded and stored inside the atoms.
  If an exception is thrown by the function this is caught, recorded as a response
  nested in a map under the key :thrown, and rethrown."
  ([] (spy (constantly nil)))
  ([f] (let [calls (atom [])
             responses (atom [])
             record-call! (fn [args] (swap! calls conj args))
             record-response! (fn [response] (swap! responses conj response))
             record-exception! (fn [e]
                                 (swap! responses
                                        conj
                                        {:thrown e}))]
         (with-meta (fn [& args]
                      (record-call! args)
                      (try
                        (let [response (apply f args)]
                          (record-response! response)
                          response)
                        (catch js/Object e
                          (record-exception! e)
                          (throw e))))
           {:calls calls
            :responses responses}))))

(deftest new-email-beatle-test
  (testing "A message is sent to a Beatle"
    ;; example 1 - wrap the original fn (so it is still called)
    (with-redefs [send-message (spy send-message)]
      (email-beatle :ringo "Hello Ringo!")
      ;; (is (spy/called-once? send-message))
      ;; (is (spy/called-with? send-message "ringo.starr@beatles.com" "Hello Ringo!"))
      )))


(comment
  (run-tests))