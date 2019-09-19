(ns chessjure.pieces-test
  (:require [clojure.test :refer :all]
            [chessjure.pieces :refer :all]))

(deftest raw-piece-test
  (is (= :rook
         (raw-piece :black-rook)))
  (is (= :bishop
         (raw-piece :black-bishop)))
  (is (= :knight
         (raw-piece :black-knight)))
  (is (= :queen
         (raw-piece :black-queen)))
  (is (= :king
         (raw-piece :black-king)))
  (is (= :pawn
         (raw-piece :black-pawn)))
  (is (= :rook
         (raw-piece :white-rook)))
  (is (= :bishop
         (raw-piece :white-bishop)))
  (is (= :knight
         (raw-piece :white-knight)))
  (is (= :queen
         (raw-piece :white-queen)))
  (is (= :king
         (raw-piece :white-king)))
  (is (= :pawn
         (raw-piece :white-pawn))))
