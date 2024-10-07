# Table of contents
-  [`com.wallbrew.spoon.compatibility`](#com.wallbrew.spoon.compatibility)  - Functions that provide compatibility with older versions of Clojure.
    -  [`update-keys`](#com.wallbrew.spoon.compatibility/update-keys) - Return <code>m</code> with <code>f</code> applied to each key in <code>m</code> with its <code>args</code>.
    -  [`update-vals`](#com.wallbrew.spoon.compatibility/update-vals) - Return <code>m</code> with <code>f</code> applied to each val in <code>m</code> with its <code>args</code>.
-  [`com.wallbrew.spoon.core`](#com.wallbrew.spoon.core)  - General purpose utility functions.
    -  [`concatv`](#com.wallbrew.spoon.core/concatv) - Concatenates the given sequences together into a vector.
    -  [`filter-by-keys`](#com.wallbrew.spoon.core/filter-by-keys) - Return <code>m</code> with only the key:value pairs whose keys cause <code>pred</code> to evaluate truthily.
    -  [`filter-by-values`](#com.wallbrew.spoon.core/filter-by-values) - Return <code>m</code> with only the key:value pairs whose values cause <code>pred</code> to evaluate truthily.
    -  [`remove-by-keys`](#com.wallbrew.spoon.core/remove-by-keys) - Return <code>m</code> with only the key:value pairs whose keys cause <code>pred</code> to evaluate falsily.
    -  [`remove-by-values`](#com.wallbrew.spoon.core/remove-by-values) - Return <code>m</code> with only the key:value pairs whose values cause <code>pred</code> to evaluate falsily.
    -  [`when-let+`](#com.wallbrew.spoon.core/when-let+) - A multiple bindings version of <code>clojure.core/when-let</code>.
-  [`com.wallbrew.spoon.spec`](#com.wallbrew.spoon.spec)  - Fuctions used in conjunction with <code>clojure.spec.alpha</code>.
    -  [`test-valid?`](#com.wallbrew.spoon.spec/test-valid?) - Tests if <code>value</code> is a valid instance of <code>spec</code>.
-  [`com.wallbrew.spoon.string`](#com.wallbrew.spoon.string)  - Functions for working with strings.
    -  [`->slug`](#com.wallbrew.spoon.string/->slug) - Take a string <code>s</code> and return a [slug-ified](https://en.wikipedia.org/wiki/Clean_URL#Slug) string.
    -  [`->spongebob-case`](#com.wallbrew.spoon.string/->spongebob-case) - Take a string <code>s</code> and coerce characters alternatively between lower and upper case.
    -  [`->sporadic-case`](#com.wallbrew.spoon.string/->sporadic-case) - Take a string <code>s</code> and randomly coerce characters to either lower or upper case.
    -  [`cast-to-uppercase?`](#com.wallbrew.spoon.string/cast-to-uppercase?) - An option map key to cast strings to UPPER CASE in <code>prepare-for-compare</code>.
    -  [`includes?`](#com.wallbrew.spoon.string/includes?) - Checks to see if <code>s1</code> includes <code>s2</code> after each string has been modified by <code>prepare-for-compare</code>.
    -  [`not-blank?`](#com.wallbrew.spoon.string/not-blank?) - Takes a string <code>s</code> and returns false if is <code>s</code> is nil, the empty string, or contains only whitespace.
    -  [`same-text?`](#com.wallbrew.spoon.string/same-text?) - Checks to see if <code>s1</code> and <code>s2</code> are equal after each string has been <code>trim</code>ed and cast to the same casing.
-  [`com.wallbrew.spoon.version`](#com.wallbrew.spoon.version)  - Tools to inspect and compare Clojure versions.
    -  [`->printable-clojure-version`](#com.wallbrew.spoon.version/->printable-clojure-version) - Returns clojure version as a printable string.
    -  [`assert-minimum-clojure-version!`](#com.wallbrew.spoon.version/assert-minimum-clojure-version!) - Assert that the current version of Clojure is at least <code>min-version</code>.

-----
# <a name="com.wallbrew.spoon.compatibility">com.wallbrew.spoon.compatibility</a>


Functions that provide compatibility with older versions of Clojure.

   This allows libraries to be used in projects that are not yet on the latest version of Clojure.




## <a name="com.wallbrew.spoon.compatibility/update-keys">`update-keys`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.compatibility/update-keys"></a>
``` clojure

(update-keys m f & args)
```


Return `m` with `f` applied to each key in `m` with its `args`.
   A version of this function was added to `clojure.core` in release 1.11;
     however, many libraries included this function either in their API or their implementation.
   This leads consumers to continually receive warnings about shadowed functionality;
     however, libraries cannot leverage the version in `clojure.core` without breaking compatibility for consumers using older versions of clojure.

   Example:
   ```clj
   (update-keys* {:a 2 :b 3} name) ; => {"a" 2 "b" 3}
   (update-keys* {} dec) ; => {}
   (update-keys* {:b 3 :c 4} str "-key") ; => {":b-key" 3 ":c-key" 4}
   ```

## <a name="com.wallbrew.spoon.compatibility/update-vals">`update-vals`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.compatibility/update-vals"></a>
``` clojure

(update-vals m f & args)
```


Return `m` with `f` applied to each val in `m` with its `args`.
   A version of this function was added to `clojure.core` in release 1.11;
     however, many libraries included this function either in their API or their implementation.
   This leads consumers to continually receive warnings about shadowed functionality;
     however, libraries cannot leverage the version in `clojure.core` without breaking compatibility for consumers using older versions of clojure.

   Example:
   ```clj
   (update-vals* {:a 1 :b 2} inc) ; => {:a 2 :b 3}
   (update-vals* {} dec) ; => {}
   (update-vals* {:b 1 :c 2} + 2) ; => {:b 3 :c 4}
   ```

-----
# <a name="com.wallbrew.spoon.core">com.wallbrew.spoon.core</a>


General purpose utility functions.




## <a name="com.wallbrew.spoon.core/concatv">`concatv`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.core/concatv"></a>
``` clojure

(concatv & vectors)
```


Concatenates the given sequences together into a vector.
   Provided as an alternative to `concat`, when a lazy sequence would be inappropriate.

   Example:
   ```clj
   (concatv [1 2] [3 4]) ; => [1 2 3 4]
   (concat [1] [2] '(3 4) [5 6 7] #{9 10 8}) ; => (1 2 3 4 5 6 7 8 9 10)
   ```

## <a name="com.wallbrew.spoon.core/filter-by-keys">`filter-by-keys`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.core/filter-by-keys"></a>
``` clojure

(filter-by-keys pred m)
```


Return `m` with only the key:value pairs whose keys cause `pred` to evaluate truthily.

   Example:
   ```clj
   (filter-by-keys nil? {}) ; => {}
   (filter-by-keys keyword? {:a 2 "b" 1 :c 4 :d 6 "e" 7}) ; => {:a 2 :c 4 :d 6}
   ```

## <a name="com.wallbrew.spoon.core/filter-by-values">`filter-by-values`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.core/filter-by-values"></a>
``` clojure

(filter-by-values pred m)
```


Return `m` with only the key:value pairs whose values cause `pred` to evaluate truthily.

   Example:
   ```clj
   (filter-by-values nil? {}) ; => {}
   (filter-by-values even? {:a 2 :b 1 :c 4 :d 6 :e 7}) ; => {:a 2 :c 4 :d 6}
   ```

## <a name="com.wallbrew.spoon.core/remove-by-keys">`remove-by-keys`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.core/remove-by-keys"></a>
``` clojure

(remove-by-keys pred m)
```


Return `m` with only the key:value pairs whose keys cause `pred` to evaluate falsily.

   Example:
   ```clj
   (remove-by-keys nil? {}) ; => {}
   (remove-by-keys keyword? {:a 2 "b" 1 :c 4 :d 6 "e" 7}) ; => {"b" 1 "e" 7}
   ```

## <a name="com.wallbrew.spoon.core/remove-by-values">`remove-by-values`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.core/remove-by-values"></a>
``` clojure

(remove-by-values pred m)
```


Return `m` with only the key:value pairs whose values cause `pred` to evaluate falsily.

   Example:
   ```clj
   (remove-by-values nil? {}) ; => {}
   (remove-by-values even? {:a 2 :b 1 :c 4 :d 6 :e 7}) ; => {:b 1 :e 7}
   ```

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
   If the bindings vector contains an invalid number of forms, an assertion error will be thrown.

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




## <a name="com.wallbrew.spoon.string/->slug">`->slug`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.string/->slug"></a>
``` clojure

(->slug s)
```


Take a string `s` and return a [slug-ified](https://en.wikipedia.org/wiki/Clean_URL#Slug) string.

     For example:

     ```clj
       (->slug "Nick's recipe" "nicks-recipe")
     ```

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

## <a name="com.wallbrew.spoon.string/cast-to-uppercase?">`cast-to-uppercase?`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.string/cast-to-uppercase?"></a>

An option map key to cast strings to UPPER CASE in [`prepare-for-compare`](#com.wallbrew.spoon.string/prepare-for-compare).
   Commonly, this is set for the `options` argument of `same?` and [`includes?`](#com.wallbrew.spoon.string/includes?).
   This option will be enabled if this key's value is truthy, and is disabled by default.

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

## <a name="com.wallbrew.spoon.string/not-blank?">`not-blank?`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.string/not-blank?"></a>
``` clojure

(not-blank? s)
```


Takes a string `s` and returns false if is `s` is nil, the empty string, or contains only whitespace.

   Example:
   ```clj
   (not-blank? "") ; => false
   (not-blank? "  ") ; => false
   (not-blank? nil) ; => false
   (not-blank? "Hello, there") ; => true
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

-----
# <a name="com.wallbrew.spoon.version">com.wallbrew.spoon.version</a>


Tools to inspect and compare Clojure versions.




## <a name="com.wallbrew.spoon.version/->printable-clojure-version">`->printable-clojure-version`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.version/->printable-clojure-version"></a>
``` clojure

(->printable-clojure-version {:keys [major minor incremental qualifier interim], :as _version})
```


Returns clojure version as a printable string.

   For example:
   ```clj
   (->printable-clojure-version {:major 1 :minor 12 :incremental 0 :qualifier nil}) ; => "1.12.0"
   (->printable-clojure-version {:major 1 :minor 12 :incremental 0 :qualifier "alpha"}) ; => "1.12.0-alpha"
   (->printable-clojure-version {:major 1 :minor 12 :incremental 0 :qualifier "alpha" :interim 1}) ; => "1.12.0-alpha-SNAPSHOT"
   ```

## <a name="com.wallbrew.spoon.version/assert-minimum-clojure-version!">`assert-minimum-clojure-version!`</a> [:page_facing_up:](null)
<a name="com.wallbrew.spoon.version/assert-minimum-clojure-version!"></a>
``` clojure

(assert-minimum-clojure-version! {:keys [major minor incremental qualifier], :as min-version})
```


Assert that the current version of Clojure is at least `min-version`.
   If the versions are incompatible, it throws an assertion error with a message indicating the incompatibility.
   If the versions are, or may be compatible, it returns a keyword indicating the compatibility level:

     - `:safe` - The Semantic Versions of the current and minimum versions are compatible.
     - `:warn` - The Semantic Versions of the current and minimum versions may be incompatible.
                 For example, a major version bump in the language version may introduce breaking changes in the API.
                 However, the current version may still be compatible with the minimum version.

   `min-version` should be a map with the same structure as the dynamic var `clojure-version`.
   For example, the dependency `[org.clojure/clojure "1.12.0"]` would translate to:
   ```clj
   *clojure-version*
    ; => {:major 1, :minor 12, :incremental 0, :qualifier nil}
   ```

   This function is useful for libraries that require a minimum version of Clojure to function properly.
