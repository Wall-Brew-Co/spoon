(ns com.wallbrew.spoon.version-test
  (:require [clojure.test :refer [deftest is testing]]
            [com.wallbrew.spoon.version :as sut]))


(deftest ->printable-clojure-version-test
  (testing "Functional correctness"
    (is (string? (sut/->printable-clojure-version (sut/current-clojure-version))))
    (is (= #_{:clj-kondo/ignore [:unresolved-symbol]}
           (clojure-version)
           (sut/->printable-clojure-version (sut/current-clojure-version))))
    (is (= "1.12.0"
           (sut/->printable-clojure-version {:major       1
                                             :minor       12
                                             :incremental 0})))
    (is (= "1.12.0-alpha"
           (sut/->printable-clojure-version {:major       1
                                             :minor       12
                                             :incremental 0
                                             :qualifier   "alpha"})))
    (is (= "1.12.0-alpha-SNAPSHOT"
           (sut/->printable-clojure-version {:major       1
                                             :minor       12
                                             :incremental 0
                                             :qualifier   "alpha"
                                             :interim     true})))
    (is (= "1.12.0-SNAPSHOT"
           (sut/->printable-clojure-version {:major       1
                                             :minor       12
                                             :incremental 0
                                             :qualifier   nil
                                             :interim     true})))))


(deftest jvm-assert-minimum-clojure-version!-test
  (let [sample-version {:major       1
                        :minor       2
                        :incremental 3
                        :qualifier   nil}]
    (testing "Versions are identical"
      (with-redefs [sut/current-clojure-version (constantly sample-version)]
        (is (= :safe
               (sut/assert-minimum-clojure-version! sample-version)))))
    (testing "The current major version is greater than the minimum major version"
      (with-redefs [sut/current-clojure-version (constantly (update sample-version :major inc))]
        (is (= :warn
               (sut/assert-minimum-clojure-version! sample-version)))))
    (testing "Minimum major version is greater than current major version"
      (with-redefs [sut/current-clojure-version (constantly sample-version)]
        (is (thrown? AssertionError
              (sut/assert-minimum-clojure-version! (update sample-version :major inc))))))
    (testing "Minimum minor version is greater than current minor version"
      (with-redefs [sut/current-clojure-version (constantly sample-version)]
        (is (thrown? AssertionError
              (sut/assert-minimum-clojure-version! (update sample-version :minor inc))))))
    (testing "Minimum incremental version is greater than current incremental version"
      (with-redefs [sut/current-clojure-version (constantly sample-version)]
        (is (thrown? AssertionError
              (sut/assert-minimum-clojure-version! (update sample-version :incremental inc))))))
    (testing "Minimum version includes a qualifier, but current version does not"
      (with-redefs [sut/current-clojure-version (constantly sample-version)]
        (is (thrown? AssertionError
              (sut/assert-minimum-clojure-version! (assoc sample-version :qualifier "alpha"))))))
    (testing "Minimum version and current version include a qualifier, but they are not equal"
      (with-redefs [sut/current-clojure-version (constantly (assoc sample-version :qualifier "beta"))]
        (is (thrown? AssertionError
              (sut/assert-minimum-clojure-version! (assoc sample-version :qualifier "alpha"))))))))
