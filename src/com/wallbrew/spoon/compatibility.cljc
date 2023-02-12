(ns com.wallbrew.spoon.compatibility
  "Functions that provide compatibility with older versions of Clojure.

   This allows libraries to be used in projects that are not yet on the latest version of Clojure."
  {:added "1.2"}
  (:refer-clojure :exclude [update-vals update-keys]))


(defn update-vals
  "Return `m` with `f` applied to each val in `m` with its `args`.
   A version of this function was added to `clojure.core` in release 1.11;
     however, many libraries included this function either in their API or their implementation.
   This leads consumers to continually receive warnings about shadowed functionality;
     however, libraries cannot leverage the version in `clojure.core` without breaking compatibility for consumers using older versions of clojure.
   
   Example:
   ```clj
   (update-vals* {:a 1 :b 2} inc) ; => {:a 2 :b 3}
   (update-vals* {} dec) ; => {}
   (update-vals* {:b 1 :c 2} + 2) ; => {:b 3 :c 4}
   ```"
  {:added    "1.2"
   :see-also ["clojure.core/update-vals"
              "update-keys"]}
  [m f & args]
  (reduce-kv (fn [m' k v] (assoc m' k (apply f v args))) {} m))


(defn update-keys
  "Return `m` with `f` applied to each key in `m` with its `args`.
   A version of this function was added to `clojure.core` in release 1.11;
     however, many libraries included this function either in their API or their implementation.
   This leads consumers to continually receive warnings about shadowed functionality;
     however, libraries cannot leverage the version in `clojure.core` without breaking compatibility for consumers using older versions of clojure.
   
   Example:
   ```clj
   (update-keys* {:a 2 :b 3} name) ; => {\"a\" 2 \"b\" 3}
   (update-keys* {} dec) ; => {}
   (update-keys* {:b 3 :c 4} str \"-key\") ; => {\":b-key\" 3 \":c-key\" 4}
   ```"
  {:added    "1.2"
   :see-also ["clojure.core/update-keys"
              "update-vals"]}
  [m f & args]
  (reduce-kv (fn [m' k v] (assoc m' (apply f k args) v)) {} m))
