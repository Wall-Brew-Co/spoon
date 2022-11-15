(ns com.wallbrew.spoon.spec-test
  (:require [clojure.spec.alpha :as spec]
            [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [com.wallbrew.spoon.spec :as sut]))


(spec/def ::int int?)
(spec/def ::string (spec/and string? #(not (str/blank? %))))
(spec/def ::sample (spec/keys :req-un [::int] :opt-un [::string]))


(deftest test-valid?-test
  (testing "test-valid? returns true if the value is valid"
    (is (true? (sut/test-valid? ::int 1)))
    (is (true? (sut/test-valid? ::string "a")))
    (is (true? (sut/test-valid? ::sample {:int 1})))
    (is (true? (sut/test-valid? ::sample {:int    1
                                          :string "a"}))))
  (testing "test-valid? returns a string if the value is invalid"
    (is (string? (sut/test-valid? ::int "a")))
    (is (string? (sut/test-valid? ::string "")))
    (is (string? (sut/test-valid? ::sample {:int "a"})))
    (is (string? (sut/test-valid? ::sample {:int    1
                                            :string ""})))
    (is (string? (sut/test-valid? ::sample {:string "hello"}))))
  (testing "test-valid? returns a string that explains why the value is invalid"
    (is (= "\"a\" - failed: int? spec: :com.wallbrew.spoon.spec-test/int\n"
           (sut/test-valid? ::int "a")))
    (is (= "\"\" - failed: (not (blank? %)) spec: :com.wallbrew.spoon.spec-test/string\n"
           (sut/test-valid? ::string "")))
    (is (= "\"a\" - failed: int? in: [:int] at: [:int] spec: :com.wallbrew.spoon.spec-test/int\n"
           (sut/test-valid? ::sample {:int "a"})))
    (is (= "\"\" - failed: (not (blank? %)) in: [:string] at: [:string] spec: :com.wallbrew.spoon.spec-test/string\n"
           (sut/test-valid? ::sample {:int    1
                                      :string ""})))
    (is (= "{:string \"hello\"} - failed: (contains? % :int) spec: :com.wallbrew.spoon.spec-test/sample\n"
           (sut/test-valid? ::sample {:string "hello"})))))
