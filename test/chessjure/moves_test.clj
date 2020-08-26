(ns chessjure.moves-test
  (:require [clojure.test :refer :all]
            [chessjure.board :refer :all]
            [chessjure.moves :refer :all]))

#_(deftest move-rook
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

(deftest manipulate-position
  (testing "alter the field position"
    (is (= (add-to-pos [:a :1] [1 1]) [:b :2]))
    (is (= (add-to-pos [:a :1] [2 2]) [:c :3]))
    (is (= (add-to-pos [:c :3] [-2 -2]) [:a :1]))
    (is (= (add-to-pos [:a :1] [-1 1]) [nil :2]))
    (is (= (add-to-pos [:a :1] [1 -1]) [:b nil]))
    (is (= (add-to-pos [:a :1] [2 1]) [:c :2])
        "knights move")
    (is (= (add-to-pos [:h :8] [1 1]) [nil nil]))))
