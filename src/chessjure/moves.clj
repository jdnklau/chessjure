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

(def col-index {:a 0
                :b 1
                :c 2
                :d 3
                :e 4
                :f 5
                :g 6
                :h 7})

(def row-index {:1 0
                :2 1
                :3 2
                :4 3
                :5 4
                :6 5
                :7 6
                :8 7})

(defn add-to-pos
  "Shifts the values of the `pos` by `addc` in the column and `addr` in the
   row. `addc` and `addr` are integers."
 [[col row :as pos] [addc addr]]
  (let [ridx (row-index row)
        cidx (col-index col)]
    [(nth col-keys (+ cidx addc) nil)
     (nth row-keys (+ ridx addr) nil)]))
