(ns com.wallbrew.spoon.core-test
  (:require [clojure.spec.alpha :as spec]
            [clojure.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :as check.test]
            [clojure.test.check.generators :as generate]
            [clojure.test.check.properties :as prop]
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
  (testing "when-let+ implements vacuous truthiness in its bindings. This is the same behavior as `let`"
    (is (= :empty
           (sut/when-let+ [] :empty)
           (let [] :empty)))))


(deftest concatv-test
  (testing "When given no sequences, concatv returns an empty vector."
    (is (= [] (sut/concatv))))
  (testing "When given a single sequence, concatv returns that sequence as a vector."
    (is (= [1 2 3] (sut/concatv [1 2 3])))
    (is (= [1 2 3] (sut/concatv '(1 2 3))))
    (is (= [1 2 3] (sort (sut/concatv #{1 2 3}))))
    (is (= [[:a "b"] [:c "d"]]
           (sut/concatv {:a "b" :c "d"}))))
  (testing "When given multiple sequences, concatv returns a vector containing all the elements of the sequences."
    (is (= [1 2 3 4 5 6 7 8 9 10] (sut/concatv [1 2 3] [4 5 6] '(7 8 9) [10])))
    (is (= [1 2 3] (sut/concatv [1] [2] [] [] '(3))))
    (is (= [1 2 [3 4] 3] (sut/concatv [1] [2 [3 4]] [] [] '(3))))))


(deftest filter-by-values-test
  (testing "Only k-v pairs whose values when applied to f are truthy remains"
    (is (= {} (sut/filter-by-values some? {})))
    (is (= {} (sut/filter-by-values nil? {})))
    (is (= {:a 2 :c 4 :d 6} (sut/filter-by-values even? {:a 2 :b 1 :c 4 :d 6 :e 7})))
    (is (= {:a nil} (sut/filter-by-values nil? {:a nil :b {:c nil}})))
    (is (= {"a" 1 "b" 3 "c" 5} (sut/filter-by-values odd? {"a" 1 "b" 3 "c" 5})))
    (is (= {} (sut/filter-by-values map? {:a [] :b 5 :c "hello"})))))


(deftest remove-by-values-test
  (testing "Only k-v pairs whose values when applied to f are falsey remains"
    (is (= {} (sut/remove-by-values some? {})))
    (is (= {} (sut/remove-by-values nil? {})))
    (is (= {:b 1 :e 7} (sut/remove-by-values even? {:a 2 :b 1 :c 4 :d 6 :e 7})))
    (is (= {:b {:c nil}} (sut/remove-by-values nil? {:a nil :b {:c nil}})))
    (is (= {} (sut/remove-by-values odd? {"a" 1 "b" 3 "c" 5})))
    (is (= {:a [] :b 5 :c "hello"} (sut/remove-by-values map? {:a [] :b 5 :c "hello"})))))


(deftest filter-by-keys-test
  (testing "Only k-v pairs whose keys when applied to f are truthy remains"
    (is (= {} (sut/filter-by-keys some? {})))
    (is (= {} (sut/filter-by-keys nil? {})))
    (is (= {2 :a 4 :c 6 :d} (sut/filter-by-keys even? {2 :a 1 :b 4 :c 6 :d 7 :e})))
    (is (= {nil :a} (sut/filter-by-keys nil? {nil :a :b {:c nil}})))
    (is (= {"a" 1 "b" 3 "c" 5} (sut/filter-by-keys string? {"a" 1 "b" 3 "c" 5})))
    (is (= {} (sut/filter-by-keys string? {:a [] :b 5 :c "hello"})))))


(deftest remove-by-keys-test
  (testing "Only k-v pairs whose keys when applied to f are falsey remains"
    (is (= {} (sut/remove-by-keys some? {})))
    (is (= {} (sut/remove-by-keys nil? {})))
    (is (= {1 :b 7 :e} (sut/remove-by-keys even? {2 :a 1 :b 4 :c 6 :d 7 :e})))
    (is (= {:b {:c nil}} (sut/remove-by-keys nil? {nil :a :b {:c nil}})))
    (is (= {} (sut/remove-by-keys string? {"a" 1 "b" 3 "c" 5})))
    (is (= {:a [] :b 5 :c "hello"} (sut/remove-by-keys string? {:a [] :b 5 :c "hello"})))))


(deftest submap?-test
  (testing "\"Smaller\" maps are submaps of matching larger maps"
    (is (true? (sut/submap? {} {})))
    (is (true? (sut/submap? {} {:a 1})))
    (is (true? (sut/submap? {:a 1} {:a 1})))
    (is (true? (sut/submap? {:a {:b 1}} {:c 3 :a {:b 1 :d 2}})))
    (is (true? (sut/submap? {:a {:b {:c 2 :cee 3}}}
                            {:a {:bee 2 :b {:c 2 :cee 3}}})))
    (is (true? (sut/submap? {nil "foo" :a false :b nil} {nil "foo" :bar 3 :baz false :a false :b nil})))
    (is (false? (sut/submap? {:a 1} {})))
    (is (false? (sut/submap? {:a 1} {:b 1})))
    (is (false? (sut/submap? {:a {:b {:c 2 :eff "gee"}}}
                             {:a {:bee 2 :b {:c 2 :cee 3}}})))))


(spec/def ::map map?)

#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  submap?-type-test 500
  (prop/for-all
    [m1 (generate/map generate/any-equatable generate/any-equatable)
     m2 (generate/map generate/any-equatable generate/any-equatable)]
    (boolean? (sut/submap? m1 m2))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  filter-by-keys-gen-test 500
  (prop/for-all
    [m1 (generate/map generate/any-equatable generate/any-equatable)]
    (if (or (contains? m1 nil) (contains? m1 false))
      (= (dissoc m1 nil false)
         (sut/filter-by-keys identity m1))
      (= m1 (sut/filter-by-keys identity m1)))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  remove-by-keys-gen-test 500
  (prop/for-all
    [m1 (generate/map generate/any-equatable generate/any-equatable)]
    (if (or (contains? m1 nil) (contains? m1 false))
      (= (select-keys m1 [nil false])
         (sut/remove-by-keys identity m1))
      (empty? (sut/remove-by-keys identity m1)))))
