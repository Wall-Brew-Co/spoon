(ns com.wallbrew.spoon.core
  "General purpose utility functions."
  {:added "1.0"})


;; Flow Control

(defmacro when-let+
  "A multiple bindings version of `clojure.core/when-let`.
   If all bindings evaluate truthy, the body will be evaluated in an implicit `do` in which all bindings are bound to the value of their test.
   If any binding evaluates falsey, the body will not be evaluated and nil will be returned.
   If multiple forms are provided, the last form will be returned.
   
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
   :see-also ["clojure.core/when-let"]}
  [bindings & body]
  (assert (even? (count bindings)))
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
