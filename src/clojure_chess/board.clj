(ns clojure-chess.board)

(def col-keys
  [:a :b :c :d :e :f :g :h])
(def row-keys
  [:1 :2 :3 :4 :5 :6 :7 :8])

(def black-pieces
  [:black-rook :black-knight :black-bishop :black-queen :black-king :black-pawn])

(def white-pieces
  [:white-rook :white-knight :white-bishop :white-queen :white-king :white-pawn])

(def empty-row (reduce #(assoc %1 %2 :empty) {} col-keys))

(def empty-board
  (reduce #(assoc %1 %2 empty-row) {} row-keys))

(defn board-put
  "Puts a given piece into the given position"
  [board piece row col]
  (assoc-in board [row col] piece))

(defn- position-pawns
  [board]
  (let
   [set-pawns (fn [board pawn row]
                (reduce #(board-put %1 pawn row %2) board col-keys))]
    (-> board
        (set-pawns :white-pawn :2)
        (set-pawns :black-pawn :7))))

(defn- position-opposite
  ([board white-piece black-piece col]
   (-> board
       (board-put white-piece :1 col)
       (board-put black-piece :8 col)))
  ([board white-piece black-piece col1 col2]
   (-> board
       (board-put white-piece :1 col1)
       (board-put white-piece :1 col2)
       (board-put black-piece :8 col1)
       (board-put black-piece :8 col2))))

(def initial-board
  "Board with initial positioning of pieces"
  (-> empty-board
      (position-pawns)
      (position-opposite :white-rook :black-rook :a :h)
      (position-opposite :white-knight :black-knight :b :g)
      (position-opposite :white-bishop :black-bishop :c :f)
      (position-opposite :white-queen :black-queen :d)
      (position-opposite :white-king :black-king :e)))
