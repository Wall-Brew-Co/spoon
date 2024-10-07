(ns com.wallbrew.spoon.version
  "Tools to inspect and compare Clojure versions."
  {:added "1.4"})


(defn current-clojure-version
  "Returns the current version of Clojure as a map.
   This is a shim to resolve the dynamic var `*clojure-version*` and sidestep a clj-kondo warning."
  {:added "1.4"
   :no-doc true}
  []
  #_{:clj-kondo/ignore [:unresolved-symbol]}
  *clojure-version*)


(defn ->printable-clojure-version
  "Returns clojure version as a printable string.

   For example:
   ```clj
   (->printable-clojure-version {:major 1 :minor 12 :incremental 0 :qualifier nil}) ; => \"1.12.0\"
   (->printable-clojure-version {:major 1 :minor 12 :incremental 0 :qualifier \"alpha\"}) ; => \"1.12.0-alpha\"
   (->printable-clojure-version {:major 1 :minor 12 :incremental 0 :qualifier \"alpha\" :interim 1}) ; => \"1.12.0-alpha-SNAPSHOT\"
   ```"
  {:added "1.4"
   :see-also ["clojure.core/clojure-version"]}
  [{:keys [major minor incremental qualifier interim] :as _version}]
  (str major
       "."
       minor
       (when-let [i incremental]
         (str "." i))
       (when-let [q qualifier]
         (when (pos? (count q))
           (str "-" q)))
       (when interim
         "-SNAPSHOT")))


(defn assert-minimum-clojure-version!
  "Assert that the current version of Clojure is at least `min-version`.
   If the versions are incompatible, it throws an assertion error with a message indicating the incompatibility.
   If the versions are, or may be compatible, it returns a keyword indicating the compatibility level:

     - `:safe` - The Semantic Versions of the current and minimum versions are compatible.
     - `:warn` - The Semantic Versions of the current and minimum versions may be incompatible.
                 For example, a major version bump in the language version may introduce breaking changes in the API.
                 However, the current version may still be compatible with the minimum version.

   `min-version` should be a map with the same structure as the dynamic var `clojure-version`.
   For example, the dependency `[org.clojure/clojure \"1.12.0\"]` would translate to:
   ```clj
   *clojure-version*
    ; => {:major 1, :minor 12, :incremental 0, :qualifier nil}
   ```

   This function is useful for libraries that require a minimum version of Clojure to function properly."
  {:added    "1.4"
   :see-also ["clojure.core/clojure-version"
              "clojure.core/*clojure-version*"]}
  [{:keys [major minor incremental qualifier] :as min-version}]
  (let [current-version     (current-clojure-version)
        current-major       (:major current-version)
        current-minor       (:minor current-version)
        current-incremental (:incremental current-version)
        current-qualifier   (:qualifier current-version)
        comparison (cond
                     (< major current-major) :warn
                     (> major current-major) :error
                     (> minor current-minor) :error
                     (> incremental current-incremental) :error
                     (and qualifier (nil? current-qualifier)) :error
                     (and qualifier (not= qualifier current-qualifier)) :error
                     :else :safe)]
    (assert (not= :error comparison)
            (str "The current Clojure version "
                 (->printable-clojure-version current-version)
                 " is not compatible with the minimum required version "
                 (->printable-clojure-version min-version)))
    comparison))
