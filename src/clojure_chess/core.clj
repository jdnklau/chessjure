(ns clojure-chess.core
  (:gen-class))

(def col-keys
  [:a :b :c :d :e :f :g :h])
(def row-keys
  [:1 :2 :3 :4 :5 :6 :7 :8])

(def empty-row (reduce #(assoc %1 %2 :empty) {} col-keys))

(def empty-board
  (reduce #(assoc %1 %2 empty-row) {} row-keys))

(def piece-visuals
  {:black-empty "#"
   :white-empty "_"})

(defn coloured-empty
  [row col]
  (let [row-even? (reduce #(or %1 %2) false (map #(= row %) [:2 :4 :6 :8]))
        in-even-blacks? (reduce #(or %1 %2) false (map #(= col %) [:b :d :f :h]))]
    (if (and row-even? in-even-blacks?)
      :black-empty
      (if (not (or row-even? in-even-blacks?))
        :black-empty
        :white-empty))))

(defn visualise-piece
  [board row col]
  (let [piece (get-in board [row col])]
    (piece-visuals (if (= piece :empty)
                     (coloured-empty row col)
                     piece))))

(defn visualise-row
  [board row]
  (map str (map #(visualise-piece board row %1) col-keys)))

(defn print-board
  [board]
  (println " " (apply str col-keys))
  (doall
   (map
    #(println %1 (clojure.string/join " " (visualise-row board %1)))
    row-keys)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (print-board empty-board)
  0)
