(ns com.wallbrew.spoon.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [com.wallbrew.spoon.core :as sut]))


(deftest when-let+-test
  (testing "when-let+ allows for multiple binding forms"
    (is (= 1 (sut/when-let+ [a 1] a))
        "when-let+ can bind a single value. This is the same as when-let.")
    (is (= :c (sut/when-let+ [a 1] a :b :c))
        "when-let+ returns the last value in the body. This is the same as when-let.")
    (is (= 3 (sut/when-let+ [a 1 b 2] (+ a b)))
        "when-let+ can bind multiple values. When both bound values are truthy, the body is evaluated an the result is returned")
    (is (nil? (sut/when-let+ [a nil b 2] (+ a b)))
        "when-let+ can bind multiple values. When the first bound value is nil, the body is not evaluated and nil is returned")
    (is (nil? (sut/when-let+ [a false b 2] (+ a b)))
        "when-let+ can bind multiple values. When the first bound value is false, the body is not evaluated and nil is returned")
    (is (nil? (sut/when-let+ [a 1 b nil] (+ a b)))
        "when-let+ can bind multiple values. When any bound value is nil, the body is not evaluated and nil is returned")
    (is (nil? (sut/when-let+ [a (get {:c nil} :c) b 2] (+ a b)))
        "when-let+ can bind multiple values. When any bound value evaluates to nil, the body is not evaluated and nil is returned"))
  (testing "when-let+ implements vacuous truthiness in its bidnings. This is the same behavior as `let`"
    (is (= :empty (sut/when-let+ [] :empty)))))
