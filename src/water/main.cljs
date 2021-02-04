(ns water.main)

(declare draw-ctx)

(defn rect [x y width height]
  (.fillRect draw-ctx x y width height))

(defn line [x1 y1 x2 y2]
  (doto draw-ctx
    (.beginPath)
    (.moveTo x1 y1)
    (.lineTo x2 y2)
    (.stroke)))

(defn main-loop [ctx width height]
  (let [])
  (rect (- width 200) 100 100 100)
  (line 0 0 100 100))

(defn start-loop [ctx width-atom height-atom]
  (letfn [(looper []
            (main-loop ctx @width-atom @height-atom)
            (js/requestAnimationFrame looper))]
    (looper)))

(defn main []
  (let [canvas (js/document.getElementById "canvas")
        ctx (.getContext canvas "2d")
        window-width (atom (.-innerWidth js/window))
        window-height (atom (.-innerHeight js/window))]
    (set! draw-ctx ctx)
    (doto canvas
      (.setAttribute "width" @window-width)
      (.setAttribute "height" @window-height))
    (.addEventListener js/window "resize"
      (fn [_e]
        (swap! window-width (.-innerWidth js/window))
        (swap! window-height (.-innerHeight js/window))))
    
    (start-loop ctx window-width window-height)))

(defn ^:dev/after-load reload []
  (main))
