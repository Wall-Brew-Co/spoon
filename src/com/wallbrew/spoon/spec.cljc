(ns com.wallbrew.spoon.spec
  "Fuctions used in conjunction with `clojure.spec.alpha`."
  {:added "1.0"}
  (:require [clojure.spec.alpha :as spec]))


(defn test-valid?
  "Tests if `value` is a valid instance of `spec`.
   Returns true if `value` is valid.
   Otherwise, returns the value of `clojure.spec.alpha/explain-str`.
   
   Useful in tests to check if a value is valid, and to get feedback when tests fail.
   
   Example:
   ```clj
   (test-valid? ::int? 1) ; => true
   (test-valid? ::int? \"1\") ; => \"spec: :com.wallbrew.spoon.spec/int? fails predicate: int? with: \\\"1\\\"\"
   ```"
  {:added    "1.0"
   :see-also ["clojure.spec.alpha/explain-str" "clojure.spec.alpha/valid?"]}
  [spec value]
  (if (spec/valid? spec value)
    true
    (spec/explain-str spec value)))
