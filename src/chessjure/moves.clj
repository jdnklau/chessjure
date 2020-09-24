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
        cidx (col-index col)
        new-col (nth col-keys (+ cidx addc) nil)
        new-row (nth row-keys (+ ridx addr) nil)]
    (cond
      (nil? new-col) nil
      (nil? new-row) nil
      :else [new-col new-row])))

(defn exceeding-edge? [[col row] [dx dy]]
  (cond
    (and (= col :a) (< dx 0)) true
    (and (= col :h) (> dx 0)) true
    (and (= row :1) (< dy 0)) true
    (and (= row :8) (> dy 0)) true
    :else false))

(defn remaining-cols [col dx]
  "Returns the remaining columns into the given direction.
   `dx<0` yields smaller columns, `dx>0` yields greater columns.
   For `dx=0`, the current column is returned. Otherwise it is always excluded."
  (cond
    (= dx 0) [col]
    (< dx 0) (subvec col-keys 0 (col-index col))
    (> dx 0) (subvec col-keys (inc (col-index col)))))

(defn remaining-rows [row dy]
  "Returns the remaining rowumns into the given direction.
   `dy<0` yields smaller rowumns, `dy>0` yields greater rowumns.
   For `dy=0`, the current rowumn is returned. Otherwise it is always excluded."
  (cond
    (= dy 0) [row]
    (< dy 0) (subvec row-keys 0 (row-index row))
    (> dy 0) (subvec row-keys (inc (row-index row)))))

(defn count-cols [col dx]
  "Returns the number of cols remaining into the given direction.
   `dx<0` yields smaller columns, `dx>0` yields greater columns."
  (let [idx (col-index col)]
    (if (neg? dx)
      idx
      (- 7 idx))))

(defn count-rows [row dy]
  "Returns the number of rows remaining into the given direction.
   `dy<0` yields smaller columns, `dy>0` yields greater columns."
  (let [idx (row-index row)]
    (if (neg? dy)
      idx
      (- 7 idx))))

(defn sgn [x]
  "Returns the sign of an integer."
  (case
   (neg? x) -1
   (pos? x) 1
   :else 0))

(defn line-of-sight
  "Yields a set of all fields not hidden behind other pieces from a given board
   position's point of view.
   `dx` and `dy` hereby dictate the view direction.
   For `dx<0` the check follows towards lower columns, for `dx>0` higher columns.
   For `dy<0` the check follows towards lower rows, for `dy>0` higher rows."
  [board [col row :as pos] [dx dy :as dir]]
  (loop [[c r] pos
         result #{}]
    (let [new-pos (add-to-pos [c r] [dx dy])]
      (cond
        (nil? new-pos) result
        (not= :empty (board-get board new-pos)) (conj result new-pos)
        :else (recur new-pos (conj result new-pos))))))
