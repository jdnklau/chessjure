(ns chessjure.player
  (:require [chessjure.board :refer :all]
            [chessjure.moves :refer :all]
            [chessjure.game-logic :refer :all]
            [clojure.string :as str])
  (:gen-class))

(defn parse-move [input]
  (let [[c1 r1 c2 r2] (remove #(= (keyword " ") %1)
                              (map (comp keyword str)
                                   (str/lower-case input)))]
    [[c1 r1] [c2 r2]]))

(defn query-player
  "Queries the respective player for their move. Returns updated board."
  [board colour]
  (loop []
    (println "Please enter a move for" colour)
    (println "Give current figurine position and target, e.g. b4 d5.")
    (let [[from to] (parse-move (read-line))
          pieces (player-positions board colour)]
      (if (and (contains? pieces from)
               (valid-move? board from to))
        (move board from to)
        (do
          (println "Erroneous input or invalid move.")
          (recur))))))
