(ns com.wallbrew.spoon.string-test
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :as check.test]
            [clojure.test.check.generators :as generate]
            [clojure.test.check.properties :as prop]
            [com.wallbrew.spoon.string :as sut]))


(spec/def ::string string?)


(deftest same-text?-test
  (testing "same-text? is a function from a tuple of strings to a boolean"
    (is (boolean? (sut/same-text? (gen/generate (spec/gen ::string)) (gen/generate (spec/gen ::string))))))
  (testing "Strings containing matching characters after preparation match"
    (is (true? (sut/same-text? "   clojure" "CLOJURE   ")))
    (is (true? (sut/same-text? "clojure   " "   CLOJURE   " {:upper-case? true})))
    (is (true? (sut/same-text? "   100 LINES OF CODE" "100 LINES OF CODE   ")))
    (is (false? (sut/same-text? "clo jure" "CLOJURE")))
    (is (false? (sut/same-text? "100" "!))" {:upper-case? true})))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  same-text?-spec 100
  (prop/for-all
    [s1 generate/string
     s2 generate/string]
    (boolean? (sut/same-text? s1 s2))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  same-text?-spec-upper-case 100
  (prop/for-all
    [s1 generate/string
     s2 generate/string]
    (boolean? (sut/same-text? s1 s2 {:upper-case? true}))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  same-text?-spec-lower-case 100
  (prop/for-all
    [s1 generate/string
     s2 generate/string]
    (boolean? (sut/same-text? s1 s2 {:upper-case? false}))))


(deftest includes?-test
  (testing "same-text? is a function from a tuple of strings to a boolean"
    (is (boolean? (sut/includes? (gen/generate (spec/gen ::string)) (gen/generate (spec/gen ::string))))))
  (testing "Strings containing matching characters after preparation match"
    (is (true? (sut/includes? "" "")))
    (is (true? (sut/includes? "   clojure" "CLOJURE   ")))
    (is (true? (sut/includes? "CLOJURE   " "c")))
    (is (true? (sut/includes? "clojure   " "   CLOJURE   " {:upper-case? true})))
    (is (true? (sut/includes? "100 LINES OF CODE   " "   100 ")))
    (is (false? (sut/includes? "clo" "CLOJURE")))
    (is (false? (sut/includes? "100" "!))" {:upper-case? true})))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  includes?-spec 100
  (prop/for-all
    [s1 generate/string
     s2 generate/string]
    (boolean? (sut/includes? s1 s2))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  includes?-spec-upper-case 100
  (prop/for-all
    [s1 generate/string
     s2 generate/string]
    (and (boolean? (sut/includes? s1 s2 {:upper-case? true}))
         (boolean? (sut/includes? s1 s2 {sut/cast-to-uppercase? true})))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  includes?-spec-lower-case 100
  (prop/for-all
    [s1 generate/string
     s2 generate/string]
    (and (boolean? (sut/includes? s1 s2 {:upper-case? false}))
         (boolean? (sut/includes? s1 s2 {:sut/cast-to-uppercase? false})))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  ->spongebob-case-type 100
  (prop/for-all
    [s generate/string]
    (string? (sut/->spongebob-case s))))


#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  ->sporadic-case-type 100
  (prop/for-all
    [s generate/string]
    (string? (sut/->sporadic-case s))))


(deftest ->sporadic-case-test
  (testing "->sporadic-case is a function from a string to a string"
    (is (string? (sut/->sporadic-case (gen/generate (spec/gen ::string))))))
  (testing "->sporadic-case, for English text, returns a `same-text?` string"
    (let [s "This is a test string"]
      (is (sut/same-text? s (sut/->sporadic-case s)))))
  (testing "Blank strings are returned as-is"
    (is (= "" (sut/->sporadic-case "")))
    (is (= " " (sut/->sporadic-case " ")))))


(deftest ->spongebob-case-test
  (testing "->spongebob-case is a function from a string to a string"
    (is (string? (sut/->spongebob-case (gen/generate (spec/gen ::string))))))
  (testing "->spongebob-case, for English text, returns a `same-text?` string"
    (let [s "This is a test string"]
      (is (sut/same-text? s (sut/->spongebob-case s)))
      (is (= "tHiS Is a tEsT StRiNg" (sut/->spongebob-case s)))))
  (testing "Blank strings are returned as-is"
    (is (= "" (sut/->spongebob-case "")))
    (is (= " " (sut/->spongebob-case " ")))))


#?(:clj
   (deftest ->slug-test
     (testing "->slug can modify non-special strings"
       (is (= "charlie-brown" (sut/->slug "charlie brown")))
       (is (= "charlie-brown" (sut/->slug "Charlie Brown")))
       (is (= "charlie-brown" (sut/->slug "   Charlie Brown   "))))
     (testing "->slug coerces non-ascii characters to their closest equivalent"
       (is (= "aaaaaaaaaa" (sut/->slug "áÁàÀãÃâÂäÄ")))
       (is (= "eeeeeeeeee" (sut/->slug "éÉèÈẽẼêÊëË")))
       (is (= "iiiiiiiiii" (sut/->slug "íÍìÌĩĨîÎïÏ")))
       (is (= "oooooooooo" (sut/->slug "óÓòÒõÕôÔöÖ")))
       (is (= "uuuuuuuuuu" (sut/->slug "úÚùÙũŨûÛüÜ")))
       (is (= "cccccc" (sut/->slug "ćĆĉĈçÇ"))))
     (testing "->slug is a function from a string to a string"
       (is (string? (sut/->slug (gen/generate (spec/gen ::string))))))
     (testing "->slug returns an identical value when called on a slug"
       (let [s (gen/generate (spec/gen ::string))]
         (is (= (sut/->slug s) (sut/->slug (sut/->slug s))))))))

#_{:clj-kondo/ignore [:unresolved-symbol]}


(check.test/defspec
  not-blank?-type 100
  (prop/for-all
   [s1 generate/string]
   (boolean? (sut/not-blank? s1))))

(deftest not-blank?-test
  (testing "not-blank? is a function from a string to a boolean"
    (is (boolean? (sut/not-blank? (gen/generate (spec/gen ::string))))))
  (testing "not-blank? returns true for non-blank strings"
    (is (true? (sut/not-blank? "a")))
    (is (true? (sut/not-blank? "a ")))
    (is (true? (sut/not-blank? " a")))
    (is (true? (sut/not-blank? " a "))))
  (testing "not-blank? returns false for blank strings"
    (is (false? (sut/not-blank? "")))
    (is (false? (sut/not-blank? " ")))
    (is (false? (sut/not-blank? "     ")))
    (is (false? (sut/not-blank? "   
                                 ")))))
