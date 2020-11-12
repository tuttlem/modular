(ns modular.core)

(def rack (atom {:modules [] :cables []}))

(defn process-rack [dt t]
  (Thread/sleep 100))

(defn animate [dt t nt]
  "Internal time animation for the rack and its devices"
  (println (str "dt: " dt ", t: " t))
  (process-rack dt t)
  (let [nnt (System/nanoTime)
        d (- nnt nt)]
    (future
      (animate d (+ d t) nnt)))
    nil)

;; Start the system animation process as soon as possible
(animate 0 0 (System/nanoTime))

(defn add-module
  "Adds a module to the rack"
  [device]
  (swap!
    rack
    update-in [:modules] conj device))

(defn vco
  "Create a new VCO into the rack"
  [name]
  (add-module {:name name :device :vco :pitch 0}))

(defn lfo
  "Create a new LFO into the rack"
  [name]
  (add-module {:name name :device :lfo :speed 0}))

(defn add-cable
  "Places a cable between two devices"
  [dev1 port1 dev2 port2]
  (let [cable {:dev1 dev1 :port1 port1
               :dev2 dev2 :port2 port2}]
  (swap!
    rack
    update-in [:cables] conj cable)))

;; create a voltage controlled oscillator
(vco "vco1")

;; create a low frequency oscillator
(lfo "lfo1")

;; modulate the vco's pitch, with the lfo
(add-cable "lfo1" "sine-out" "vco1" "pitch")
