(ns com.wallbrew.spoon.core
  "General purpose utility functions."
  {:added "1.0"})


;; Flow Control

(defmacro when-let+
  "A multiple bindings version of `clojure.core/when-let`.
   If all bindings evaluate truthy, the body will be evaluated in an implicit `do` in which all bindings are bound to the value of their test.
   If any binding evaluates falsey, the body will not be evaluated and nil will be returned.
   If multiple forms are provided, the last form will be returned.
   If the bindings vector contains an invalid number of forms, an assertion error will be thrown.

   Example:
   ```clj
   (when-let+
     [a 1 b 2]
     (+ a b)) ; => 3

   (when-let+
     [a nil b 2]
     (+ a b)) ; => nil
   ```"
  {:added    "1.0"
   :changed  "1.4"
   :see-also ["clojure.core/when-let"]}
  [bindings & body]
  (assert (even? (count bindings))
          "`when-let+` requires an even number of forms in the bindings vector.")
  (if (seq bindings)
    `(when-let [~(first bindings) ~(second bindings)]
       (when-let+ ~(vec (drop 2 bindings)) ~@body))
    `(do ~@body)))


;; Sequences

(defn concatv
  "Concatenates the given sequences together into a vector.
   Provided as an alternative to `concat`, when a lazy sequence would be inappropriate.

   Example:
   ```clj
   (concatv [1 2] [3 4]) ; => [1 2 3 4]
   (concat [1] [2] '(3 4) [5 6 7] #{9 10 8}) ; => (1 2 3 4 5 6 7 8 9 10)
   ```"
  {:added    "1.2"
   :see-also ["clojure.core/concat"]}
  [& vectors]
  (vec (apply concat vectors)))


;; Hash Maps

(defn filter-by-values
  "Return `m` with only the key:value pairs whose values cause `pred` to evaluate truthily.

   Example:
   ```clj
   (filter-by-values nil? {}) ; => {}
   (filter-by-values even? {:a 2 :b 1 :c 4 :d 6 :e 7}) ; => {:a 2 :c 4 :d 6}
   ```"
  {:added    "1.2"
   :see-also ["clojure.core/filter"
              "filter-by-keys"
              "remove-by-values"
              "remove-by-keys"]}
  [pred m]
  (letfn [(reducing-fn [m k v] (if (pred v) (assoc m k v) m))]
    (reduce-kv reducing-fn {} m)))


(defn filter-by-keys
  "Return `m` with only the key:value pairs whose keys cause `pred` to evaluate truthily.

   Example:
   ```clj
   (filter-by-keys nil? {}) ; => {}
   (filter-by-keys keyword? {:a 2 \"b\" 1 :c 4 :d 6 \"e\" 7}) ; => {:a 2 :c 4 :d 6}
   ```"
  {:added    "1.2"
   :see-also ["clojure.core/filter"
              "filter-by-values"
              "remove-by-values"
              "remove-by-keys"]}
  [pred m]
  (letfn [(reducing-fn [m k v] (if (pred k) (assoc m k v) m))]
    (reduce-kv reducing-fn {} m)))


(defn remove-by-values
  "Return `m` with only the key:value pairs whose values cause `pred` to evaluate falsily.

   Example:
   ```clj
   (remove-by-values nil? {}) ; => {}
   (remove-by-values even? {:a 2 :b 1 :c 4 :d 6 :e 7}) ; => {:b 1 :e 7}
   ```"
  {:added    "1.2"
   :see-also ["clojure.core/remove"
              "remove-by-keys"
              "filter-by-values"
              "filter-by-keys"]}
  [pred m]
  (letfn [(reducing-fn [m k v] (if (pred v) m (assoc m k v)))]
    (reduce-kv reducing-fn {} m)))


(defn remove-by-keys
  "Return `m` with only the key:value pairs whose keys cause `pred` to evaluate falsily.

   Example:
   ```clj
   (remove-by-keys nil? {}) ; => {}
   (remove-by-keys keyword? {:a 2 \"b\" 1 :c 4 :d 6 \"e\" 7}) ; => {\"b\" 1 \"e\" 7}
   ```"
  {:added    "1.2"
   :see-also ["clojure.core/remove"
              "remove-by-values"
              "filter-by-values"
              "filter-by-keys"]}
  [pred m]
  (letfn [(reducing-fn [m k v] (if (pred k) m (assoc m k v)))]
    (reduce-kv reducing-fn {} m)))


(defn submap?
  "Returns true is `map1` is a \"submap\" of `map2`.
   Meaning that `map2` contains all keys of `map1`, and that the values at those keys are either equal of themselves submaps.
   Short-circuits on the first failed comparison.
   
   Example:
   ```clj
   (submap? {} {:a 1}) ; true
   (submap? {:a {:b 1}} {:c 3 :a {:b 1 :d 2}}) ; true
   (submap? {:a 1} {:a 1 b 2 :c 3}) ; true
   (submap? {:f 4} {:a 1 b 2 :c 3}) ; false
   (submap? {:a 1 :c 3} {:a 1 b 2 :c 3}) ; true
   ```"
  {:added "1.5"}
  [map1 map2]
  (letfn [(submap-check
            [acc k v]
            (if (contains? map2 k)
              (if (and (map? v) (map (get map2 k)))
                (submap? v (get map2 k))
                (if (= v (get map2 k))
                  acc
                  (reduced false)))
              (reduced false)))]
    (reduce-kv submap-check true map1)))
