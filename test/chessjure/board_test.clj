(ns chessjure.board-test
  (:require [clojure.test :refer :all]
            [chessjure.board :refer :all]))

(deftest get-piece-test
  (testing "Retrieve from fresh board"
    (is (= :white-pawn
           (board-get initial-board :2 :a))
        "White pawn on A1")
    (is (= :black-pawn
           (board-get initial-board :7 :a))
        "Black pawn on A8")
    (is (= :black-queen
           (board-get initial-board :8 :d))
        "Black queen")
    (is (= :empty
           (board-get initial-board :4 :d))
        "Retrieve empty position")))
