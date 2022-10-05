(ns com.wallbrew.spoon.core
  "General purpose utility functions."
  {:added "1.0"})


(defmacro when-let+
  "A multiple bindings version of `clojure.core/when-let`.
   If all bindings evaluate truthy, the body will be evaluated in an implictit `do` in which all bindings are bound to the value of their test.
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
