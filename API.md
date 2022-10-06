# Table of contents
-  [`com.wallbrew.spoon.core`](#com.wallbrew.spoon.core)  - General purpose utility functions.
    -  [`when-let+`](#com.wallbrew.spoon.core/when-let+) - A multiple bindings version of <code>clojure.core/when-let</code>.
-  [`com.wallbrew.spoon.spec`](#com.wallbrew.spoon.spec)  - Fuctions used in conjunction with <code>clojure.spec.alpha</code>.
    -  [`test-valid?`](#com.wallbrew.spoon.spec/test-valid?) - Tests if <code>value</code> is a valid instance of <code>spec</code>.
-  [`com.wallbrew.spoon.string`](#com.wallbrew.spoon.string)  - Functions for working with strings.
    -  [`->spongebob-case`](#com.wallbrew.spoon.string/->spongebob-case) - Take a string <code>s</code> and coerce characters alternatively between lower and upper case.
    -  [`->sporadic-case`](#com.wallbrew.spoon.string/->sporadic-case) - Take a string <code>s</code> and randomly coerce characters to either lower or upper case.
    -  [`includes?`](#com.wallbrew.spoon.string/includes?) - Checks to see if <code>s1</code> includes <code>s2</code> after each string has been modified by <code>prepare-for-compare</code>.
    -  [`same-text?`](#com.wallbrew.spoon.string/same-text?) - Checks to see if <code>s1</code> and <code>s2</code> are equal after each string has been <code>trim</code>ed and cast to the same casing.

-----
# <a name="com.wallbrew.spoon.core">com.wallbrew.spoon.core</a>


General purpose utility functions.




## <a name="com.wallbrew.spoon.core/when-let+">`when-let+`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.core/when-let+"></a>
``` clojure

(when-let+ bindings & body)
```


Macro.


A multiple bindings version of `clojure.core/when-let`.
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
   ```

-----
# <a name="com.wallbrew.spoon.spec">com.wallbrew.spoon.spec</a>


Fuctions used in conjunction with `clojure.spec.alpha`.




## <a name="com.wallbrew.spoon.spec/test-valid?">`test-valid?`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.spec/test-valid?"></a>
``` clojure

(test-valid? spec value)
```


Tests if `value` is a valid instance of `spec`.
   Returns true if `value` is valid.
   Otherwise, returns the value of `clojure.spec.alpha/explain-str`.
   
   Useful in tests to check if a value is valid, and to get feedback when tests fail.
   
   Example:
   ```clj
   (test-valid? ::int? 1) ; => true
   (test-valid? ::int? "1") ; => "spec: :com.wallbrew.spoon.spec/int? fails predicate: int? with: \"1\""
   ```

-----
# <a name="com.wallbrew.spoon.string">com.wallbrew.spoon.string</a>


Functions for working with strings.




## <a name="com.wallbrew.spoon.string/->spongebob-case">`->spongebob-case`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.string/->spongebob-case"></a>
``` clojure

(->spongebob-case s)
```


Take a string `s` and coerce characters alternatively between lower and upper case.

   For example:

   ```clj
    (->spongebob-case "spongebob") ;; => "sPoNgEbOb"
   ```

## <a name="com.wallbrew.spoon.string/->sporadic-case">`->sporadic-case`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.string/->sporadic-case"></a>
``` clojure

(->sporadic-case s)
```


Take a string `s` and randomly coerce characters to either lower or upper case.

   For example:

   ```clj
   (->sporadic-case "hello world") ;; => "hElLo wOrLd"
   (->sporadic-case "hello world") ;; => "hElLo world"
   ```

## <a name="com.wallbrew.spoon.string/includes?">`includes?`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.string/includes?"></a>
``` clojure

(includes? s1 s2)
(includes? s1 s2 opts)
```


Checks to see if `s1` includes `s2` after each string has been modified by [`prepare-for-compare`](#com.wallbrew.spoon.string/prepare-for-compare).

   An option map may be passed as an optional second argument.
     The following keys are supported:

   - `:uppercase?` - If true, `s1` and `s2` will be coerced to upper case. Defaults to false.
   
   Example:
   ```clj
   (includes? "  Hello  " "hello") ; => true
   (includes? "  Hello there " "hello" {:uppercase? true}) ; => true
   (includes? "  Hello  " "goodbye" {:uppercase? false}) ; => false
    ```

## <a name="com.wallbrew.spoon.string/same-text?">`same-text?`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.string/same-text?"></a>
``` clojure

(same-text? s1 s2)
(same-text? s1 s2 opts)
```


Checks to see if `s1` and `s2` are equal after each string has been `trim`ed and cast to the same casing.

   An option map may be passed as an optional second argument.
     The following keys are supported:

   - `:uppercase?` - If true, `s1` and `s2` will be coerced to upper case. Defaults to false.
   
   Example:
   ```clj
   (same-text? "  Hello  " "hello") ; => true
   (same-text? "  Hello  " "hello" {:uppercase? true}) ; => true
   (same-text? "  Hello  " "goodbye" {:uppercase? false}) ; => false
   ```
