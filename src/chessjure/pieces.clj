(ns chessjure.pieces)

(def pieces
  [:rook :knight :bishop :queen :king :pawn])

(def black-pieces
  [:black-rook :black-knight :black-bishop :black-queen :black-king :black-pawn])

(def white-pieces
  [:white-rook :white-knight :white-bishop :white-queen :white-king :white-pawn])

(defn raw-piece
  "Return the raw piece belonging the given coloured piece."
  [coloured-piece]
  ({:black-rook :rook
    :black-knight :knight
    :black-bishop :bishop
    :black-queen :queen
    :black-king :king
    :black-pawn :pawn
    :white-rook :rook
    :white-knight :knight
    :white-bishop :bishop
    :white-queen :queen
    :white-king :king
    :white-pawn :pawn} coloured-piece))
