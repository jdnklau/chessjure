(ns chessjure.game-logic
  (:require [chessjure.moves :refer :all]
            [chessjure.pieces :refer :all]
            [chessjure.board :refer :all]))

(defn attack-map
  "Returns a nested map (column, row) yielding sets of attacked positions
  of the respectively positioned pieces."
  [board]
  (reduce #(assoc-in %1 %2 (possible-moves board %2))
          {}
          (for [c col-keys r row-keys] [c r])))


;; FIXME: Instead of checking each and every position on the board for whether
;; it attacks `pos`, it might be more performant to only check positions from
;; which an attack actually is possible in the first place.
;; These would be at most a queens+knights+kings-move away.
;; TODO: Check for en-passant.
(defn get-attackers
  "Returns a set of pieces' positions, which attack `pos`.
  `colour` (either `:black` or `:white`) can be optionally specified
  for checking empty fields to be under attack by the given `colour`"
  ([board pos]
   (get-attackers board pos
                  (opposite-colour (piece-colour (board-get board pos)))))
  ([board [col row :as pos] colour]
   (let [attacks (attack-map board)]
     (reduce
      #(if (and (contains? (get-in attacks %2) pos)
                (= (piece-colour (board-get board %2)) colour))
         (conj %1 %2)
         %1)
      #{}
      (for [c col-keys r row-keys] [c r])))))

(defn attacked?
  "Checks whether the given `pos` is under attack by the specified `colour`"
  [board pos colour]
  (let [attacks (get-attackers board pos colour)]
    (< 0 (count attacks))))


(defn find-king [board colour]
  "Returns the position of the king of specified `colour`."
  (let [king (case colour :white :white-king :black :black-king)]
    (first (remove nil? (for [c (keys board) r (keys (board c {}))]
                          (if (= king (board-get board c r))
                            [c r]
                            nil))))))


(defn in-check?
  "Checks whether the given `colour` is currently in check."
  [board colour]
  (attacked? board (find-king board colour) (opposite-colour colour)))

(defn resolves-check?
  "Checks whether the move from `piece-pos` to `target-pos` resolves
   the in-check for the given player.
   Also false if the player would get into check although he was not before."
  [board colour piece-pos target-pos]
  ;; Peek at following state and check whether in-check? remains true.
  (not (in-check? (move board piece-pos target-pos) colour)))


;; FIXME: Maybe follow a directional approach with
;; moves/line-of-sight function
;; so we do not need to calculate all possible moves for a piece but only
;; possible moves into the indicated direction.
;; Would then be a multimethod approach like the moves/possible-moves function
;; is.
(defn valid-move? [board [col row :as pos] [tarcol tarrow :as target]]
  ;; Do not capture kings directly.
  (if (king? (board-get board target))
    false
    (and (contains? (possible-moves board pos) target)
         (resolves-check? board
                          (piece-colour (board-get board pos))
                          pos target))))


(defn check-mate?
  [board colour]
  (let [pieces (player-positions board colour)
        pos-moves (map (comp (partial apply hash-map)
                             #(vector % (possible-moves board %)))
                       (player-positions board colour))]
    (not (some true? (for [ms pos-moves
                           from (keys ms)
                           to (ms from)]
                       (if (valid-move? board from to)
                         true ; Valid move found
                         nil))))))
