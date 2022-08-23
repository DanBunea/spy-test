(ns spy-test
  (:require [clojure.test :refer [deftest testing is run-tests]]
            [email-sender :refer [send-message email-beatle]]
            [spy.core :as spy]))


(deftest email-beatle-test
  (testing "A message is sent to a Beatle"
    ;; example 1 - wrap the original fn (so it is still called)
    (with-redefs [send-message (spy/spy send-message)]
      (email-beatle :ringo "Hello Ringo!")
      (is (spy/called-once? send-message))
      (is (spy/called-with? send-message "ringo.starr@beatles.com" "Hello Ringo!"))))

  (testing "A message is not sent to a Rolling Stone"
    ;; example 2 - call spy without passing a fn (to avoid sending the email)
    (with-redefs [send-message (spy/spy)]
      (email-beatle :mick "Hello Mr Jagger!")
      (is (spy/not-called? send-message)))))


(deftest non-spy
  (testing "non spy"
    (is (nil? (send-message "ringo.starr@beatles.com" "Hello Ringo!")))))