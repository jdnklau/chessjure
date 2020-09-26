(ns chessjure.moves
  (:require [chessjure.board :refer :all]
            [chessjure.pieces :refer :all]))

(defmulti valid-piece-move? (fn [board [row col] [trow tcol]]
                              (raw-piece (board-get board row col))))

(defmethod valid-piece-move? :rook [board [row col] [trow tcol]]
  (or (and (= row trow) (not= col tcol))
      (and (not= row trow) (= col tcol))))

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
        cidx (col-index col)
        new-col (nth col-keys (+ cidx addc) nil)
        new-row (nth row-keys (+ ridx addr) nil)]
    (cond
      (nil? new-col) nil
      (nil? new-row) nil
      :else [new-col new-row])))

(defn sgn [x]
  "Returns the sign of an integer."
  (case
   (neg? x) -1
   (pos? x) 1
   :else 0))

(defn- add-if-opp-colour [board colour other-piece-pos result]
  (let [opp-colour (opposite-colour colour)
        other-piece (board-get board other-piece-pos)
        other-colour (piece-colour other-piece)]
    (if (or
         (and (= opp-colour other-colour) (not (king? other-piece)))
         (not colour)) ; Other piece has the opposite colour?
      (conj result other-piece-pos)
      result)))

(defn line-of-sight
  "Yields a set of all fields not hidden behind other pieces from a given board
   position's point of view.
   `dx` and `dy` hereby dictate the view direction.
   For `dx<0` the check follows towards lower columns, for `dx>0` higher columns.
   For `dy<0` the check follows towards lower rows, for `dy>0` higher rows."
  [board [col row :as pos] [dx dy :as dir]]
  (let [this-colour (piece-colour (board-get board pos))]
    (loop [[c r] pos
           result #{}]
      (let [new-pos (add-to-pos [c r] [dx dy])]
        (cond
          (nil? new-pos) result
          (not= :empty (board-get board new-pos)) (add-if-opp-colour
                                                   board
                                                   this-colour
                                                   new-pos
                                                   result)
          :else (recur new-pos (conj result new-pos)))))))

;; TODO:
;;
;; Knight movement is special case.
;; Pawn capture is special case.
;; Rochade is special case.
;; What about check before mate?
