(ns com.wallbrew.spoon.compatibility-test
  (:require [clojure.test :refer [deftest is testing]]
            [com.wallbrew.spoon.compatibility :as sut]))


(deftest update-vals-test
  ;; N.B. These tests do not compare themselves to the clojure.core/update-vals function
  ;;      We do this so we are able to run the test suite against older versions of Clojure
  (testing "Functional correctness"
    (is (= {:a 2 :b 3}
           (sut/update-vals {:a 1 :b 2} inc)))
    (is (= {}
           (sut/update-vals {} dec)))
    (is (= {:b 3 :c 4}
           (sut/update-vals {:b 1 :c 2} + 2)))))


(deftest update-keys-test
  ;; N.B. These tests do not compare themselves to the clojure.core/update-keys function
  ;;      We do this so we are able to run the test suite against older versions of Clojure
  (testing "Functional correctness"
    (is (= {"a" 2 "b" 3}
           (sut/update-keys {:a 2 :b 3} name)))
    (is (= {}
           (sut/update-keys {} dec)))
    (is (= {":b-key" 3 ":c-key" 4}
           (sut/update-keys {:b 3 :c 4} str "-key")))))
