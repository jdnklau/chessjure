(ns chessjure.core
  (:require [chessjure.board :refer :all]
            [chessjure.player :refer :all]
            [chessjure.game-logic :refer :all])
  (:gen-class))

(def piece-visuals
  {:black-empty "#"
   :white-empty "_"
   :black-rook   "r"
   :black-knight "n"
   :black-bishop "b"
   :black-queen  "q"
   :black-king   "k"
   :black-pawn   "p"
   :white-rook   "R"
   :white-knight "N"
   :white-bishop "B"
   :white-queen  "Q"
   :white-king   "K"
   :white-pawn   "P"})

(def piece-emoji
  {:black-empty "◼️"
   :white-empty "◻️"
   :white-rook   "♖"
   :white-knight "♘"
   :white-bishop "♗"
   :white-queen  "♕"
   :white-king   "♔"
   :white-pawn   "♙"
   :black-rook   "♜"
   :black-knight "♞"
   :black-bishop "♝"
   :black-queen  "♛"
   :black-king   "♚"
   :black-pawn   "♟"})

(defn coloured-empty
  [col row]
  (let [row-even? (boolean (some #{:2 :4 :6 :8} [row]))
        in-even-blacks? (boolean (some #{:b :d :f :h} [col]))]
    (if (= row-even? in-even-blacks?)
      :black-empty
      :white-empty)))

(defn visualise-piece
  [board col row]
  (let [piece (get-in board [col row])]
    (piece-emoji (if (= piece :empty)
                   (coloured-empty col row)
                   piece))))

(defn visualise-row
  [board row]
  (map str (map #(visualise-piece board %1 row) col-keys)))

(defn print-board
  [board]
  (println " " (apply str col-keys))
  (doseq [row (reverse row-keys)]
    (println row (clojure.string/join " " (visualise-row board row)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [board initial-board]
    (print-board board)
    (if (check-mate? board :white)
      (println ":black has won!")
      (let [new-board (query-player board :white)]
        (print-board new-board)
        (if (check-mate? new-board :black)
          (println ":white has won!")
          (recur (query-player new-board :black))))))
  0)
