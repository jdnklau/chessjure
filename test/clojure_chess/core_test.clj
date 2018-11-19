(ns clojure-chess.core-test
  (:require [clojure.test :refer :all]
            [clojure-chess.core :refer :all]))

(deftest black-white-fields
  (testing "Tests whether the black and white empty fields on the board are labelled correctly"
    (do
      (is (= :black-empty (coloured-empty :1 :a)))
      (is (= :white-empty (coloured-empty :1 :b)))
      (is (= :white-empty (coloured-empty :2 :a)))
      (is (= :black-empty (coloured-empty :2 :b))))))
