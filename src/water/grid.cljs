(ns water.grid)

(defn square [] {:vector [0 0]})

(defn init-grid [rows cols]
  (let [col (-> (repeat rows (square)) vec)]
    (-> (repeat cols col) vec)))

(defn at [grid x y]
  (nth (nth grid x) y))


