(ns chessjure.board
  (:require [chessjure.pieces :refer :all]))

(def col-keys
  [:a :b :c :d :e :f :g :h])
(def row-keys
  [:1 :2 :3 :4 :5 :6 :7 :8])

(def empty-col (reduce #(assoc %1 %2 :empty) {} row-keys))

(def empty-board
  (reduce #(assoc %1 %2 empty-col) {} col-keys))

(defn board-put
  "Puts a given piece into the given position"
  ([board piece pos]
   (assoc-in board pos piece))
  ([board piece col row]
   (assoc-in board [col row] piece)))

(defn board-get
  "Returns the given piece from the board, or nil if position is empty."
  ([board pos]
   (get-in board pos))
  ([board col row]
   (get-in board [col row])))

(defn- position-pawns
  [board]
  (let
   [set-pawns (fn [board pawn row]
                (reduce #(board-put %1 pawn %2 row) board col-keys))]
    (-> board
        (set-pawns :white-pawn :2)
        (set-pawns :black-pawn :7))))

(defn- position-opposite
  ([board white-piece black-piece col]
   (-> board
       (board-put white-piece  col :1)
       (board-put black-piece  col :8)))
  ([board white-piece black-piece col1 col2]
   (-> board
       (board-put white-piece  col1 :1)
       (board-put white-piece  col2 :1)
       (board-put black-piece  col1 :8)
       (board-put black-piece  col2 :8))))

(def initial-board
  "Board with initial positioning of pieces"
  (-> empty-board
      (position-pawns)
      (position-opposite :white-rook :black-rook :a :h)
      (position-opposite :white-knight :black-knight :b :g)
      (position-opposite :white-bishop :black-bishop :c :f)
      (position-opposite :white-queen :black-queen :d)
      (position-opposite :white-king :black-king :e)))

(defn player-positions
  "Returns set of a player's pieces' positions."
  [board colour]
  (into #{} (remove nil?
                   (for [c col-keys r row-keys]
                     (if (= colour (piece-colour (board-get board c r)))
                       [c r])))))
