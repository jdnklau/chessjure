(ns chessjure.core-test
  (:require [clojure.test :refer :all]
            [chessjure.core :refer :all]))

(deftest black-white-fields
  (testing "Tests whether the black and white empty fields on the board are labelled correctly"
    (do
      (is (= :black-empty (coloured-empty :1 :a)) "a1 should be black")
      (is (= :white-empty (coloured-empty :1 :b)) "b1 should be white")
      (is (= :white-empty (coloured-empty :2 :a)) "a2 should be white")
      (is (= :black-empty (coloured-empty :2 :b)) "b2 should be black"))))
