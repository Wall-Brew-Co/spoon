(ns com.wallbrew.spoon.runner
  (:require [com.wallbrew.spoon.core-test]
            [com.wallbrew.spoon.spec-test]
            [com.wallbrew.spoon.string-test]
            [doo.runner :refer-macros [doo-tests]]))


(doo-tests 'com.wallbrew.spoon.core-test
           'com.wallbrew.spoon.spec-test
           'com.wallbrew.spoon.string-test)
