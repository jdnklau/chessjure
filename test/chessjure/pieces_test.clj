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

(deftest piece-colour-test
  (is (= :black
         (piece-colour :black-rook)))
  (is (= :black
         (piece-colour :black-bishop)))
  (is (= :black
         (piece-colour :black-knight)))
  (is (= :black
         (piece-colour :black-queen)))
  (is (= :black
         (piece-colour :black-king)))
  (is (= :black
         (piece-colour :black-pawn)))
  (is (= :white
         (piece-colour :white-rook)))
  (is (= :white
         (piece-colour :white-bishop)))
  (is (= :white
         (piece-colour :white-knight)))
  (is (= :white
         (piece-colour :white-queen)))
  (is (= :white
         (piece-colour :white-king)))
  (is (= :white
         (piece-colour :white-pawn))))

(deftest opposite-colour-test
  (is (= :white (opposite-colour :black)))
  (is (= :black (opposite-colour :white))))
