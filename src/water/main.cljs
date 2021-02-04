(ns water.main
  (:require [water.grid :as g]))

(declare draw-ctx)

(defn rect [x y width height]
  (.fillRect draw-ctx x y width height))

(defn stroke-rect [x y width height]
  (.strokeRect draw-ctx x y width height))

(defn line [x1 y1 x2 y2]
  (doto draw-ctx
    (.beginPath)
    (.moveTo x1 y1)
    (.lineTo x2 y2)
    (.stroke)))

(defn fill [color]
  (set! (.-fillStyle draw-ctx) color))

(defn stroke
  ([width color]
   (set! (.-lineWidth draw-ctx) width)
   (stroke color))
  ([color]
   (set! (.-strokeStyle draw-ctx) color)))

(defn main-loop [ctx grid width height]
  (let [grid-width (count grid)
        grid-height (count (grid 0))
        square-width (/ width grid-width)
        square-height (/ height grid-height)]
    (.clearRect ctx 0 0 width height)
    (doseq [x (range grid-width)
            y (range grid-height)
            :let [square (get-in grid [x y])
                  left-x (* x square-width)
                  top-y (* y square-height)
                  right-x (+ left-x square-width)
                  bottom-y (+ top-y square-height)
                  right-col? (zero? x)
                  top-row? (zero? y)]]
      (stroke 1 "black")
      ;; (when right-col?
      ;;   (line left-x top-y left-x bottom-y))
      ;;  (when top-row?
      ;;    (line left-x top-y right-x top-y))
      (fill "red")
      (let [rect-size 6
            rect-x (- (+ left-x (/ square-width 2)) (/ rect-size 2))
            rect-y (- (+ top-y (/ square-height 2)) (/ rect-size 2))]
        (rect rect-x rect-y rect-size rect-size))
      (line left-x bottom-y right-x bottom-y)
      (line right-x top-y right-x bottom-y))))

(defn start-loop [ctx width-atom height-atom grid-atom]
  (letfn [(looper []
            (main-loop ctx @grid-atom @width-atom @height-atom)
            (js/requestAnimationFrame looper))]
    (looper)))

(defn main []
  (let [canvas (js/document.getElementById "canvas")
        ctx (.getContext canvas "2d")
        window-width (atom (.-innerWidth js/window))
        window-height (atom (.-innerHeight js/window))
        set-canvas-attrs #(doto canvas
                            (.setAttribute "width" @window-width)
                            (.setAttribute "height" @window-height))
        grid (atom (g/init-grid 20 20))]
    (set! draw-ctx ctx)
    (set-canvas-attrs)
    (.addEventListener js/window "resize"
      (fn [_e]
        (reset! window-width (.-innerWidth js/window))
        (reset! window-height (.-innerHeight js/window))
        (set-canvas-attrs)))

    (start-loop ctx window-width window-height grid)))

(defn ^:dev/after-load reload []
  (main))
