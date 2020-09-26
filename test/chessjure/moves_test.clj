(ns chessjure.moves-test
  (:require [clojure.test :refer :all]
            [chessjure.board :refer :all]
            [chessjure.moves :refer :all]))

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
          "to the bottom left"))))

(deftest check-possible-moves
  (let [test-with (fn [piece]
                    (board-put (-> empty-board
                                   (board-put :white-pawn :b :4)
                                   (board-put :white-pawn :b :6)
                                   (board-put :white-pawn :d :6)
                                   (board-put :white-pawn :f :6)
                                   (board-put :white-pawn :f :4)
                                   (board-put :white-pawn :f :2)
                                   (board-put :white-pawn :d :2)
                                   (board-put :white-pawn :b :2))
                               piece :d :4))]
    (testing "rook movement"
      (is (=
           #{[:c :4] [:e :4]  [:d :5]  [:d :3]}
           (possible-moves (test-with :white-rook) [:d :4])))
      (is (=
           #{[:b :4] [:c :4] [:e :4] [:f :4] [:d :5] [:d :6] [:d :3] [:d :2]}
           (possible-moves (test-with :black-rook) [:d :4]))))
    (testing "bishop movement"
      (is (=
           #{[:c :5] [:e :5] [:c :3] [:e :3]}
           (possible-moves (test-with :white-bishop) [:d :4])))
      (is (=
           #{[:c :3] [:b :2] [:e :3] [:f :2] [:c :5] [:b :6] [:e :5] [:f :6]}
           (possible-moves (test-with :black-bishop) [:d :4]))))
    (testing "queen movement"
      (is (=
           #{[:c :5] [:e :5] [:c :3] [:e :3] [:c :4] [:e :4]  [:d :5]  [:d :3]}
           (possible-moves (test-with :white-queen) [:d :4])))
      (is (=
           #{[:c :3] [:b :2] [:e :3] [:f :2] [:c :5] [:b :6] [:e :5] [:f :6]
             [:b :4] [:c :4] [:e :4] [:f :4] [:d :5] [:d :6] [:d :3] [:d :2]}
           (possible-moves (test-with :black-queen) [:d :4])))))
  (testing "white pawn movement"
    (let [test-with (fn
                      ([piece col row]
                       (board-put (-> empty-board
                                      (board-put :white-pawn :c :4)
                                      (board-put :white-pawn :e :4)
                                      (board-put :black-pawn :c :5)
                                      (board-put :black-pawn :e :5))
                                  piece col row)))]
      (is (= #{[:d :4]}
             (possible-moves (test-with :white-pawn :d :3) [:d :3]))
          "path free")
      (is (= #{}
             (possible-moves (test-with :white-pawn :e :3) [:e :3]))
          "path blocked")
      (is (= #{[:c :5] [:d :5] [:e :5]}
             (possible-moves (test-with :white-pawn :d :4) [:d :4]))
          "capture")))
  (testing "black pawn movement"
    (let [test-with (fn
                      ([piece col row]
                       (board-put (-> empty-board
                                      (board-put :white-pawn :c :4)
                                      (board-put :white-pawn :e :4)
                                      (board-put :black-pawn :c :5)
                                      (board-put :black-pawn :e :5))
                                  piece col row)))]
      (is (= #{[:d :5]}
             (possible-moves (test-with :black-pawn :d :6) [:d :6]))
          "path free")
      (is (= #{}
             (possible-moves (test-with :black-pawn :e :6) [:e :6]))
          "path blocked")
      (is (= #{[:c :4] [:d :4] [:e :4]}
             (possible-moves (test-with :black-pawn :d :5) [:d :5]))
          "capture")))
  (testing "knight movement"
    (let [test-with (fn
                      ([piece]
                       (-> empty-board
                           (board-put :white-pawn :c :2)
                           (board-put :white-pawn :c :6)
                           (board-put :white-pawn :b :3)
                           (board-put :white-pawn :b :5)
                           (board-put :white-pawn :e :2)
                           (board-put :white-pawn :e :6)
                           (board-put :white-pawn :f :3)
                           (board-put :white-pawn :f :5)
                           (board-put piece :d :4)
                           (board-put piece :e :4))))]
      (is (= #{[:d :2] [:d :6] [:c :3] [:c :5] [:f :2] [:f :6] [:g :3] [:g :5]}
             (possible-moves (test-with :white-knight) [:e :4]))
          "free spots")
      (is (= #{}
             (possible-moves (test-with :white-knight) [:d :4]))
          "no free spots")
      (is (= #{[:c :2] [:c :6] [:b :3] [:b :5] [:e :2] [:e :6] [:f :3] [:f :5]}
             (possible-moves (test-with :black-knight) [:d :4]))
          "capture")
      (is (= #{[:c :4] [:c :8] [:b :7] [:e :8] [:f :7]}
             (possible-moves (board-put
                              (test-with :white-knight)
                              :white-knight :d :6)
                             [:d :6]))
          "some free spots")))
  (testing "king movement"
    (let [test-with (fn
                      ([piece]
                       (-> empty-board
                           (board-put :white-pawn :c :3)
                           (board-put :white-pawn :c :4)
                           (board-put :white-pawn :c :5)
                           (board-put :white-pawn :d :3)
                           (board-put :white-pawn :d :5)
                           (board-put :white-pawn :e :3)
                           (board-put :white-pawn :e :4)
                           (board-put :white-pawn :e :5)
                           (board-put :white-pawn :f :3)
                           (board-put :white-pawn :f :4)
                           (board-put :white-pawn :f :5)
                           (board-put piece :d :4) ; surrounded
                           (board-put piece :g :4) ; cannot move left
                           (board-put piece :g :7) ; free movement
                           (board-put piece :a :8) ; next to king (right)
                           (board-put :black-king :c :8))))]
      (is (= #{[:f :6] [:f :7] [:f :8] [:g :6] [:g :8] [:h :6] [:h :7] [:h :8]}
             (possible-moves (test-with :white-king) [:g :7]))
          "free spots")
      (is (= #{}
             (possible-moves (test-with :white-king) [:d :4]))
          "no free spots")
      (is (= #{[:c :3] [:c :4] [:c :5] [:d :3] [:d :5] [:e :3] [:e :4] [:e :5]}
             (possible-moves (test-with :black-king) [:d :4]))
          "capture")
      (is (= #{[:g :3] [:g :5] [:h :3] [:h :4] [:h :5]}
             (possible-moves (test-with :white-king) [:g :4]))
          "some free spots")
      #_(is (= #{[:a :7]}
               (possible-moves (test-with :white-king) [:a :8]))
            "cannot get close to other kings"))))
