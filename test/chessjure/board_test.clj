(ns chessjure.board-test
  (:require [clojure.test :refer :all]
            [chessjure.board :refer :all]))

(deftest get-piece-test
  (testing "Retrieve from fresh board"
    (is (= :white-pawn
           (board-get initial-board :a :2))
        "White pawn on A1")
    (is (= :black-pawn
           (board-get initial-board :a :7))
        "Black pawn on A8")
    (is (= :black-queen
           (board-get initial-board :d :8))
        "Black queen")
    (is (= :empty
           (board-get initial-board :d :4))
        "Retrieve empty position")))

(deftest player-position-test
  (is (= #{[:e :1] [:a :1]}
         (player-positions (-> empty-board
                               (board-put :white-pawn :e :1)
                               (board-put :white-pawn :a :1)
                               (board-put :black-pawn :b :1))
                           :white))
      "White's positions")
  (is (= #{[:b :1]}
         (player-positions (-> empty-board
                               (board-put :white-pawn :e :1)
                               (board-put :white-pawn :a :1)
                               (board-put :black-pawn :b :1))
                           :black))
      "Blacks's positions"))
