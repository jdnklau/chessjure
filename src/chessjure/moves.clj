(ns chessjure.moves
  (:require [chessjure.board :refer :all]
            [chessjure.pieces :refer :all]))

(defmulti valid-piece-move? (fn [board [row col] [trow tcol]]
                              (raw-piece (board-get board row col))))

(defmethod valid-piece-move? :rook [board [row col] [trow tcol]]
  (or (and (= row trow) (not= col tcol))
      (and (not= row trow) (= col tcol))))

;; TODO: Implement movement in all directions
;; Then sample all available tiles in desired directions
;; base it on colour - what about king and check mate?
;; Knight is special case

(defn valid-move?
  "True if moving the piece on position `from` to `to` is a valid move.
   The positions are [row col] tuples."
  [board from to]
  (valid-piece-move? board
                     (apply board-get board from) ; from is a vector
                     from
                     to))
