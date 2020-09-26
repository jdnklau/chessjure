(ns chessjure.game-logic-test
  (:require [clojure.test :refer :all]
            [chessjure.board :refer :all]
            [chessjure.game-logic :refer :all]))

(deftest attack-map-test
  (let [board (-> empty-board
                  (board-put :white-rook :b :3)
                  (board-put :white-bishop :d :3)
                  (board-put :black-queen :b :5))
        attacks (attack-map board)]
    (is (= #{[:a :3] [:b :4] [:c :3] [:b :1] [:b :2] [:b :5]}
           (get-in attacks [:b :3]))
        "rook")
    (is (= #{[:c :2] [:f :1] [:b :1] [:e :4] [:b :5] [:c :4] [:g :6]
             [:h :7] [:f :5] [:e :2]}
           (get-in attacks [:d :3]))
        "queen attacks rook and bishop")))

(deftest attacks
  (testing "attacking black queen"
    (let [queen-board (-> empty-board
                          (board-put :black-queen :d :4))
          attack-with (fn [piece [col row :as pos]]
                        (-> queen-board
                            (board-put piece col row)))]
      (is (= #{} (get-attackers queen-board [:d :4]))
          "no attackers")
      (is (= #{[:d :1]}
             (get-attackers (attack-with :white-rook [:d :1]) [:d :4]))
          "one attacker")
      (is (= #{}
             (get-attackers (attack-with :black-rook [:d :1]) [:d :4]))
          "rook is same colour")))
  (testing "differing between black and white attackers"
    (let [board (-> empty-board
                    (board-put :white-bishop :a :1)
                    (board-put :white-bishop :a :3)
                    (board-put :black-bishop :c :1)
                    (board-put :black-bishop :c :3))]
      (is (= #{[:a :1] [:a :3]}
             (get-attackers board [:b :2] :white))
          "white attacks d2")
      (is (= #{[:c :1] [:c :3]}
             (get-attackers board [:b :2] :black))
          "black attacks d2")))
  (testing "fields-under-attack"
    (let [board (-> empty-board
                    (board-put :white-rook :a :1)
                    (board-put :white-rook :a :3)
                    (board-put :black-rook :c :1)
                    (board-put :black-rook :c :3))]
      (is (attacked? board [:a :1] :black)
          "piece attacked by black")
      (is (not (attacked? board [:a :1] :white))
          "piece not attacked by white")
      (is (attacked? board [:c :1] :white)
          "piece attacked by white")
      (is (not (attacked? board [:c :1] :black))
          "piece not attacked by black")
      (is (attacked? board [:c :2] :black)
          "field attacked by black")
      (is (attacked? board [:a :2] :white)
          "field not attacked by white")
      (is (attacked? board [:b :3] :black)
          "other field attacked by black")
      (is (attacked? board [:b :3] :white)
          "other field also attacked by white"))))

(deftest finding-kings
  (let [board initial-board]
    (is (= [:e :1] (find-king board :white))
        "find white king")
    (is (= [:e :8] (find-king board :black))
        "find black king")))

(deftest player-in-check
  (is (in-check? (-> empty-board
                     (board-put :black-king :a :1)
                     (board-put :white-rook :a :4))
                 :black)
      "black in check")
  (is (not (in-check? (-> empty-board
                          (board-put :black-king :a :1)
                          (board-put :white-rook :a :4))
                      :white))
      "white not in check"))

(deftest move-rook
  (testing "only rook"
    (let [board (board-put empty-board :white-rook :d :4)]
      (is (valid-move? board [:d :4] [:d :8])
          "move up")
      (is (valid-move? board [:d :4] [:d :1])
          "move down")
      (is (valid-move? board [:d :4] [:a :4])
          "move left")
      (is (valid-move? board [:d :4] [:g :4])
          "move up")
      (is (not (valid-move? board [:d :4] [:d :4]))
          "do not keep still")
      (is (not (valid-move? board [:d :4] [:e :6]))
          "do no move like knight")))
  (testing "rook with same-colour pieces around"
    (let [board (-> empty-board
                    (board-put :white-rook :d :4)
                    (board-put :white-pawn :f :4)
                    (board-put :white-pawn :d :6)
                    (board-put :white-pawn :b :4)
                    (board-put :white-pawn :d :2))]
      (is (not (valid-move? board [:d :4] [:g :4]))
          "don't pass right")))
  (testing "cannot capture king directly"
    (let [board (-> empty-board
                    (board-put :black-king :d :4)
                    (board-put :white-rook :d :1))]
      (is (not (valid-move? board [:d :1] [:d :4]))))))
