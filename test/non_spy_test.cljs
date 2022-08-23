(ns non-spy-test
  (:require [clojure.test :refer [deftest testing is run-tests]]
            [email-sender :refer [send-message]]))



(deftest non-spy
  (testing "non spy"
    (is (nil? (send-message "ringo.starr@beatles.com" "Hello Ringo!"))))
  (testing "redefs"
    (with-redefs [send-message (fn [_] :ok)]
      (is (= (send-message "ringo.starr@beatles.com" "Hello Ringo!")
             :ok)))))