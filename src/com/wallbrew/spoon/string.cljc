(ns com.wallbrew.spoon.string
  "Functions for working with strings."
  {:added "1.0"}
  (:require [clojure.string :as str])
  #?(:clj (:import [java.text Normalizer Normalizer$Form])))


(defn not-blank?
  "Takes a string `s` and returns false if is `s` is nil, the empty string, or contains only whitespace.

   Example:
   ```clj
   (not-blank? \"\") ; => false
   (not-blank? \"  \") ; => false
   (not-blank? nil) ; => false
   (not-blank? \"Hello, there\") ; => true
   ```"
  {:added    "1.2"
   :see-also ["clojure.string/blank?"]}
  [s]
  (-> s str/blank? not))


(def ^:const cast-to-uppercase?
  "An option map key to cast strings to UPPER CASE in `prepare-for-compare`.
   Commonly, this is set for the `options` argument of `same?` and `includes?`.
   This option will be enabled if this key's value is truthy, and is disabled by default."
  :uppercase?)


(defn- prepare-for-compare
  "Takes a string `s`, trims it, and coerces it to lower case.

   An option map may be passed as an optional second argument.
     The following keys are supported:

   - `:uppercase?` - If true, `s` will be coerced to upper case. Defaults to false."
  {:added    "1.0"
   :no-doc   true
   :see-also ["same?" "includes?"]}
  ([s] (prepare-for-compare s {}))

  ([^String s {:keys [uppercase?]}]
   (let [casing-fn (if uppercase? str/upper-case str/lower-case)]
     (-> s str/trim casing-fn))))


(defn same-text?
  "Checks to see if `s1` and `s2` are equal after each string has been `trim`ed and cast to the same casing.

   An option map may be passed as an optional second argument.
     The following keys are supported:

   - `:uppercase?` - If true, `s1` and `s2` will be coerced to upper case. Defaults to false.

   Example:
   ```clj
   (same-text? \"  Hello  \" \"hello\") ; => true
   (same-text? \"  Hello  \" \"hello\" {:uppercase? true}) ; => true
   (same-text? \"  Hello  \" \"goodbye\" {:uppercase? false}) ; => false
   ```"
  {:added    "1.0"
   :see-also ["includes?"
              "prepare-for-compare"
              "cast-to-uppercase?"]}
  ([s1 s2] (same-text? s1 s2 {}))

  ([^String s1 ^String s2 opts]
   (= (prepare-for-compare s1 opts)
      (prepare-for-compare s2 opts))))


(defn includes?
  "Checks to see if `s1` includes `s2` after each string has been modified by `prepare-for-compare`.

   An option map may be passed as an optional second argument.
     The following keys are supported:

   - `:uppercase?` - If true, `s1` and `s2` will be coerced to upper case. Defaults to false.

   Example:
   ```clj
   (includes? \"  Hello  \" \"hello\") ; => true
   (includes? \"  Hello there \" \"hello\" {:uppercase? true}) ; => true
   (includes? \"  Hello  \" \"goodbye\" {:uppercase? false}) ; => false
    ```"
  {:added    "1.0"
   :see-also ["includes?"
              "prepare-for-compare"
              "cast-to-uppercase?"]}
  ([s1 s2] (includes? s1 s2 {}))

  ([^String s1 ^String s2 opts]
   (str/includes? (prepare-for-compare s1 opts) (prepare-for-compare s2 opts))))


(defn ->sporadic-case
  "Take a string `s` and randomly coerce characters to either lower or upper case.

   For example:

   ```clj
   (->sporadic-case \"hello world\") ;; => \"hElLo wOrLd\"
   (->sporadic-case \"hello world\") ;; => \"hElLo world\"
   ```"
  {:added    "1.0"
   :see-also ["->spongebob-case"]}
  [^String s]
  (letfn [(random-case
            [l]
            (if (rand-nth [true false])
              (str/upper-case l)
              (str/lower-case l)))]
    (->> s
         seq
         (map random-case)
         (apply str))))


(defn ->spongebob-case
  "Take a string `s` and coerce characters alternatively between lower and upper case.

   For example:

   ```clj
    (->spongebob-case \"spongebob\") ;; => \"sPoNgEbOb\"
   ```"
  {:added    "1.0"
   :see-also ["->sporadic-case"]}
  [^String s]
  (letfn [(spongebob-case
            [acc l]
            (let [casing-fn (if (odd? (count acc)) str/upper-case str/lower-case)]
              (str acc (casing-fn l))))]
    (reduce spongebob-case "" (seq s))))


#?(:clj
   (defn ->slug
     "Take a string `s` and return a [slug-ified](https://en.wikipedia.org/wiki/Clean_URL#Slug) string.

     For example:

     ```clj
       (->slug \"Nick's recipe\" \"nicks-recipe\")
     ```"
     {:added "1.1"}
     [^String s]
     (let [normalized-s  (Normalizer/normalize s Normalizer$Form/NFD)
           asciified-s   (str/replace normalized-s #"[\P{ASCII}]+" "")
           lower-cased-s (str/lower-case asciified-s)
           split-s       (str/split (str/triml lower-cased-s) #"[\p{Space}\p{P}]+")]
       (str/join "-" split-s))))


