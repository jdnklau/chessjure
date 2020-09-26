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
    (is (= (add-to-pos [:a :1] [-1 1]) nil))
    (is (= (add-to-pos [:a :1] [1 -1]) nil))
    (is (= (add-to-pos [:a :1] [2 1]) [:c :2])
        "knights move")
    (is (= (add-to-pos [:h :8] [1 1]) nil))))

(deftest check-line-of-sight
  (testing "directions"
    (is (= #{[:a :2]} (line-of-sight empty-board [:b :2] [-1 0]))
        "to the left")
    (is (= #{[:b :1]} (line-of-sight empty-board [:b :2] [0 -1]))
        "to the bottom")
    (is (= #{[:h :7]} (line-of-sight empty-board [:g :7] [1 0]))
        "to the right")
    (is (= #{[:g :8]} (line-of-sight empty-board [:g :7] [0 1]))
        "to the top")
    (is (= #{[:a :1]} (line-of-sight empty-board [:b :2] [-1 -1]))
        "to the bottom left")
    (is (= #{[:a :8]} (line-of-sight empty-board [:b :7] [-1 1]))
        "to the top left")
    (is (= #{[:h :1]} (line-of-sight empty-board [:g :2] [1 -1]))
        "to the bottom right")
    (is (= #{[:h :8]} (line-of-sight empty-board [:g :7] [1 1]))
        "to the top right"))
  (testing "edges"
    (is (= #{} (line-of-sight empty-board [:a :2] [-1 0]))
        "at left edge going left")
    (is (= #{} (line-of-sight empty-board [:h :2] [1 0]))
        "at right edge going right")
    (is (= #{} (line-of-sight empty-board [:a :8] [0 1]))
        "at top edge going top")
    (is (= #{} (line-of-sight empty-board [:a :1] [0 -1]))
        "at bottom edge going bottom"))
  (testing "paths to edges"
    (is (= #{[:a :4] [:b :4] [:c :4]} (line-of-sight empty-board [:d :4] [-1 0]))
        "to the left")
    (is (= #{[:d :5] [:e :6] [:f :7] [:g :8]}
           (line-of-sight empty-board [:c :4] [1 1]))
        "to the top right")
    (is (= #{}
           (line-of-sight empty-board [:h :1] [1 -1]))
        "to the bottom right from respective corner"))
  (testing "path blocked by pieces"
    (let [board (-> empty-board
                    (board-put :white-pawn :b :4)
                    (board-put :white-pawn :b :6)
                    (board-put :white-pawn :d :6)
                    (board-put :white-pawn :f :6)
                    (board-put :white-pawn :f :4)
                    (board-put :white-pawn :f :2)
                    (board-put :white-pawn :d :2)
                    (board-put :white-pawn :b :2))]
      (is (= #{[:b :4] [:c :4]} (line-of-sight board [:d :4] [-1 0]))
          "to the left")
      (is (= #{[:b :6] [:c :5]} (line-of-sight board [:d :4] [-1 1]))
          "to the top left")
      (is (= #{[:d :5] [:d :6]} (line-of-sight board [:d :4] [0 1]))
          "to the top")
      (is (= #{[:e :5] [:f :6]} (line-of-sight board [:d :4] [1 1]))
          "to the top right")
      (is (= #{[:e :4] [:f :4]} (line-of-sight board [:d :4] [1 0]))
          "to the right")
      (is (= #{[:e :3] [:f :2]} (line-of-sight board [:d :4] [1 -1]))
          "to the bottom right")
      (is (= #{[:d :3] [:d :2]} (line-of-sight board [:d :4] [0 -1]))
          "to the bottom")
      (is (= #{[:b :2] [:c :3]} (line-of-sight board [:d :4] [-1 -1]))
          "to the bottom left")))
  (testing "path blocked by opposite colour pieces"
    (let [board (-> empty-board
                    (board-put :black-queen :d :4)
                    (board-put :white-pawn :b :4)
                    (board-put :white-pawn :b :6)
                    (board-put :white-pawn :d :6)
                    (board-put :white-pawn :f :6)
                    (board-put :white-pawn :f :4)
                    (board-put :white-pawn :f :2)
                    (board-put :white-pawn :d :2)
                    (board-put :white-pawn :b :2))]
      (is (= #{[:b :4] [:c :4]} (line-of-sight board [:d :4] [-1 0]))
          "to the left")
      (is (= #{[:b :6] [:c :5]} (line-of-sight board [:d :4] [-1 1]))
          "to the top left")
      (is (= #{[:d :5] [:d :6]} (line-of-sight board [:d :4] [0 1]))
          "to the top")
      (is (= #{[:e :5] [:f :6]} (line-of-sight board [:d :4] [1 1]))
          "to the top right")
      (is (= #{[:e :4] [:f :4]} (line-of-sight board [:d :4] [1 0]))
          "to the right")
      (is (= #{[:e :3] [:f :2]} (line-of-sight board [:d :4] [1 -1]))
          "to the bottom right")
      (is (= #{[:d :3] [:d :2]} (line-of-sight board [:d :4] [0 -1]))
          "to the bottom")
      (is (= #{[:b :2] [:c :3]} (line-of-sight board [:d :4] [-1 -1]))
          "to the bottom left")))
  (testing "path blocked by same colour pieces"
    (let [board (-> empty-board
                    (board-put :white-queen :d :4)
                    (board-put :white-pawn :b :4)
                    (board-put :white-pawn :b :6)
                    (board-put :white-pawn :d :6)
                    (board-put :white-pawn :f :6)
                    (board-put :white-pawn :f :4)
                    (board-put :white-pawn :f :2)
                    (board-put :white-pawn :d :2)
                    (board-put :white-pawn :b :2))]
      (is (= #{[:c :4]} (line-of-sight board [:d :4] [-1 0]))
          "to the left")
      (is (= #{[:c :5]} (line-of-sight board [:d :4] [-1 1]))
          "to the top left")
      (is (= #{[:d :5]} (line-of-sight board [:d :4] [0 1]))
          "to the top")
      (is (= #{[:e :5]} (line-of-sight board [:d :4] [1 1]))
          "to the top right")
      (is (= #{[:e :4]} (line-of-sight board [:d :4] [1 0]))
          "to the right")
      (is (= #{[:e :3]} (line-of-sight board [:d :4] [1 -1]))
          "to the bottom right")
      (is (= #{[:d :3]} (line-of-sight board [:d :4] [0 -1]))
          "to the bottom")
      (is (= #{[:c :3]} (line-of-sight board [:d :4] [-1 -1]))
          "to the bottom left")))
  (testing "path blocked by king"
    (let [board (-> empty-board
                    (board-put :black-queen :d :4)
                    (board-put :white-king :b :4))]
      (is (= #{[:c :4]} (line-of-sight board [:d :4] [-1 0]))
          "to the left"))))
