(ns chessjure.core-test
  (:require [clojure.test :refer :all]
            [chessjure.core :refer :all]))

(deftest black-white-fields
  (testing "Tests whether the black and white empty fields on the board are labelled correctly"
    (do
      (is (= :black-empty (coloured-empty :a :1)) "a1 should be black")
      (is (= :white-empty (coloured-empty :b :1)) "b1 should be white")
      (is (= :white-empty (coloured-empty :a :2)) "a2 should be white")
      (is (= :black-empty (coloured-empty :b :2)) "b2 should be black"))))
