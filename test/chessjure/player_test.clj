(ns chessjure.player-test
  (:require [clojure.test :refer :all]
            [chessjure.player :refer :all]))

(deftest parsing-player-input
  (is (= [[:a :1] [:d :7]]
         (parse-move "a1 d7"))))
