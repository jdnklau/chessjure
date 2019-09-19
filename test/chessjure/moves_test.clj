(ns chessjure.moves-test
  (:require [clojure.test :refer :all]
            [chessjure.board :refer :all]
            [chessjure.moves :refer :all]))

(deftest move-rook
  (testing "only rook"
    (let [board (board-put empty-board :white-rook :d :4)]
      (is (valid-piece-move? board [:d :4] [:d :8])
          "move up")
      (is (valid-piece-move? board [:d :4] [:d :1])
          "move down")
      (is (valid-piece-move? board [:d :4] [:a :4])
          "move left")
      (is (valid-piece-move? board [:d :4] [:g :4])
          "move up")
      (is (not (valid-piece-move? board [:d :4] [:d :4]))
          "do not keep still")
      (is (not (valid-piece-move? board [:d :4] [:e :6]))
          "do no move like knight")))
  (testing "rook with same-colour pieces around"
    (let [board (-> empty-board
                    (board-put :white-rook :d :4)
                    (board-put :white-pawn :f :4)
                    (board-put :white-pawn :d :6)
                    (board-put :white-pawn :b :4)
                    (board-put :white-pawn :d :2))]
      (is (not (valid-piece-move? board [:d :4] [:g :4]))
          "don't pass right"))))
