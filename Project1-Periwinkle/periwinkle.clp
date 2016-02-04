; Medical Diagnosis Expert System

; Import some commonly-used classes
(import javax.swing.*)
(import java.awt.*)
(import java.awt.event.*)

; Don't clear defglobals on (reset)
(set-reset-globals FALSE)

(defglobal ?*crlf* = "
")

; Question and answer templates

(deftemplate question
  (slot text)
  (slot type)
  (multislot valid)
  (slot ident))

(deftemplate answer
  (slot ident)
  (slot text))

(do-backward-chaining answer)

; Module app-rules

(defmodule app-rules)

(defrule app-rules::supply-answers
  (declare (auto-focus TRUE))
  (MAIN::need-answer (ident ?id))
  (not (MAIN::answer (ident ?id)))
  (not (MAIN::ask ?))
  =>
  (assert (MAIN::ask ?id))
  (return))

;Fever-Headache

(defrule MAIN::Fever-Headache
  "Both fever and headache are present - might be one of meningitis, mono, hepatitis, flu, malaria, ebola, hypotension"
  (declare (auto-focus TRUE))
  (answer (ident fever) (text yes))
  (answer (ident headache) (text yes))
  =>
  (assert (fever-headache yes-yes)))

;NoFever-Headache

(defrule MAIN::NoFever-Headache
  "Fever is absent but headache is present - might be cold or hypertension"
  (declare (auto-focus TRUE))
  (answer (ident fever) (text no))
  (answer (ident headache) (text yes))
  =>
  (assert (fever-headache no-yes)))

;Fever-NoHeadache

(defrule MAIN::Fever-NoHeadache
  "Fever is present but headache is absent - might be tuberculosis"
  (declare (auto-focus TRUE))
  (answer (ident fever) (text yes))
  (answer (ident headache) (text no))
  =>
  (assert (fever-headache yes-no)))

;NoFever-NoHeadache

(defrule MAIN::NoFever-NoHeadache
  "Both fever and headache are absent - might be diabetes"
  (declare (auto-focus TRUE))
  (answer (ident fever) (text no))
  (answer (ident headache) (text no))
  =>
  (assert (fever-headache no-no)))


; Hepatitis

(defrule MAIN::hepatitis
  (declare (auto-focus TRUE))
  (fever-headache yes-yes)
  (answer (ident bodypains) (text yes))
  (answer (ident hepatitis-yellow) (text yes))
  =>
  (assert (diagnosis jaundice)))
  

; Tuberculosis

(defrule MAIN::tuberculosis
  (declare (auto-focus TRUE))
  (fever-headache yes-no)
  (answer (ident tuber-cough) (text yes))
  (answer (ident appetiteloss) (text yes))
  (answer (ident tuber-sweat) (text yes))
  =>
  (assert (diagnosis tuberculosis)))

; Diabetes

(defrule MAIN::diabetes
  (declare (auto-focus TRUE))
  (fever-headache no-no)
  (answer (ident dia-vision) (text yes))
  (answer (ident dia-thirst) (text yes))
  =>
  (assert (diagnosis diabetes)))
  
; Cold

(defrule MAIN::cold
  (declare (auto-focus TRUE))
  (fever-headache no-yes)
  (answer (ident cold-stuffy) (text yes))
  =>
  (assert (diagnosis cold)))

; Hypertension

(defrule MAIN::hypertension
  (declare (auto-focus TRUE))
  (fever-headache no-yes)
  (answer (ident hyper-vertigo) (text yes))
  (answer (ident hyper-heart) (text yes))
  =>
  (recommend-action "Your symptoms indicate that you might have hypertension, commonly known as high BP.
It is strongly advised that you consult your doctor immediately.")
  (halt))

; Hypotension

(defrule MAIN::hypotension
  (declare (auto-focus TRUE))
  (fever-headache yes-yes)
  (answer (ident hypo-heart) (text yes))
  =>
  (recommend-action "Your symptoms indicate that you might have hypotension, commonly known as low BP.
Other symptoms of hypotension include chest pain and fatigue.
It is strongly advised that you consult your doctor immediately.")
  (halt))

; Meningitis

(defrule MAIN::meningitis
  (declare (auto-focus TRUE))
  (fever-headache yes-yes)
  (answer (ident menin-light) (text yes))
  (answer (ident menin-neck) (text yes))
  (answer (ident appetiteloss) (text yes))
  =>
  (assert (diagnosis meningitis)))

; Mono

(defrule MAIN::mono
  (declare (auto-focus TRUE))
  (fever-headache yes-yes)
  (answer (ident mono-swollen) (text yes))
  =>
  (assert (diagnosis mono)))

; Ebola

(defrule MAIN::ebola
  (declare (auto-focus TRUE))
  (fever-headache yes-yes)
  (answer (ident sorefatigue) (text yes))
  (answer (ident ebola-dia) (text yes))
  (answer (ident ebola-pain) (text yes))
  =>
  (assert (diagnosis ebola))
  (assert (ebola-is yes)))

; Malaria

(defrule MAIN::malaria
  (declare (auto-focus TRUE))
  (fever-headache yes-yes)
  (answer (ident mal-convul) (text yes))
  (answer (ident bodypains) (text yes))
  =>
  (assert (diagnosis malaria)))
  
; Flu

(defrule MAIN::flu
  (declare (auto-focus TRUE))
  (fever-headache yes-yes)
  (answer (ident sorefatigue) (text yes))
  (not (ebola-is yes))
  =>
  (assert (diagnosis flu)))

; No Diagnosis

(defrule MAIN::no-diabetes
  (declare (auto-focus TRUE))
  (or (answer (ident dia-vision) (text no)) (answer (ident dia-thirst) (text no)))
  =>
  (recommend-action "The expert system does not have enough/consistent facts to provide the medical diagnosis.
It is advised that you consult your doctor.")
  (halt))

(defrule MAIN::no-tuberculosis
  (declare (auto-focus TRUE))
  (or (answer (ident tuber-cough) (text no)) (answer (ident tuber-sweat) (text no)))
  =>
  (recommend-action "The expert system does not have enough/consistent facts to provide the medical diagnosis.
It is advised that you consult your doctor.")
  (halt))

/*
(defrule MAIN::others-no-diagnosis
  (declare (auto-focus TRUE))
  (or (answer (ident bodypains) (text no)) (and (answer (ident hyper-vertigo) (text no)) (answer (ident hyper-heart) (text no))))
  =>
  (recommend-action "The expert system does not have enough/consistent facts to provide the medical diagnosis.
It is advised that you consult your doctor.")
  (halt))
*/

(defrule MAIN::mono-viral
  (declare (auto-focus TRUE))
  (diagnosis mono)
  =>
  (recommend-action "Your symptoms indicate that you might have have mononucleosis, which is commonly known as mono.
Other symptoms of mono include swollen spleen, long fatigue and sore throat.
It is strongly advised that you consult your doctor immediately.")
  (halt))

(defrule MAIN::meningitis-viral-bac
  (declare (auto-focus TRUE))
  (diagnosis meningitis)
  =>
  (recommend-action "Your symptoms indicate that you might have meningitis.
This is caused by virus, bacteria or certain drugs in some cases.
Other symptoms of meningitis include nausea, vomiting, confusion or difficulty concentrating.
It is strongly advised that you consult your doctor immediately.")
  (halt))

(defrule MAIN::jaundice-liver
  (declare (auto-focus TRUE))
  (diagnosis jaundice)
  =>
  (recommend-action "Your symptoms indicate that you might have liver disease jaundice, which is an advanced stage of hepatitis.
Other symptoms of jaundice include discomfort or uneasiness, vomiting and diarrhea.
It is strongly advised that you consult your doctor immediately.")
  (halt))

(defrule MAIN::cold-viral
  (declare (auto-focus TRUE))
  (diagnosis cold)
  =>
  (recommend-action "Your symptoms indicate that you might have a common cold, which is a viral illness.
Other symptoms of cold include frequent sneezing, cough and sore throat.
It is advised that you consult your physician if the cold persists.")
  (halt))

(defrule MAIN::flu-viral
  (declare (auto-focus TRUE))
  (diagnosis flu)
  =>
  (recommend-action "Your symptoms indicate that you might have a viral illness influenza, commonly known as the flu.
Other symptoms of flu include muscle and joint pains.
It is advised that you consult your doctor.")
  (halt))

(defrule MAIN::malaria-parasitic
  (declare (auto-focus TRUE))
  (diagnosis malaria)
  =>
  (recommend-action "Your symptoms indicate that you might have malaria.
It is caused by a parasitic protozoan called Plasmodium.
Other symptoms of malaria include vomiting and fatigue.
It is strongly advised that you consult your doctor immediately.")
  (halt))

(defrule MAIN::diabetes-pancreatic
  (declare (auto-focus TRUE))
  (diagnosis diabetes)
  =>
  (recommend-action "Your symptoms indicate that you might have a pancreatic disease diabetes mellitus, commonly known as diabetes.
Other symptoms of diabetes include weight loss, frequent urination and fatigue.
It is advised that you maintain a healthy diet with reduced sugar intake and consult your doctor immediately.")
  (halt))

(defrule MAIN::tuberculosis-bac
  (declare (auto-focus TRUE))
  (diagnosis tuberculosis)
  =>
  (recommend-action "Your symptoms indicate that you might have a bacterial disease tuberculosis.
Other symptoms of tuberculosis include weight loss and fatigue.
It is strongly advised that you consult your doctor immediately.")
  (halt))

(defrule MAIN::ebola-viral
  (declare (auto-focus TRUE))
  (diagnosis ebola)
  =>
  (recommend-action "Your symptoms indicate that you might have a viral disease ebola.
Other symptoms of ebola include vomiting, diarrhea, weakness, muscle and joint pains.
It is strongly advised that you consult your doctor immediately.")
  (halt))

; Results output

(deffunction recommend-action (?action)
  "Give final instructions to the user"
  (call JOptionPane showMessageDialog ?*frame*
        (str-cat "Your medical diagnosis:
 
" ?action)
        "Medical diagnosis"
        (get-member JOptionPane INFORMATION_MESSAGE)))
  
(defadvice before halt (?*qfield* setText "THE INFERENCES MADE BY THIS EXPERT SYSTEM ARE ILLUSTRATIVE.

CLOSE THIS WINDOW TO EXIT"))

; Module ask

(defmodule ask)

(deffunction ask-user (?question ?type ?valid)
  "Set up the GUI to ask a question"
  (?*qfield* setText ?question)
  (?*apanel* removeAll)
  (if (eq ?type multi) then
    (?*apanel* add ?*acombo*)
    (?*apanel* add ?*acombo-ok*)
    (?*acombo* removeAllItems)
    (foreach ?item ?valid
             (?*acombo* addItem ?item))
    else
    (?*apanel* add ?*afield*)
    (?*apanel* add ?*afield-ok*)
    (?*afield* setText ""))
  (?*apanel* validate)
  (?*apanel* repaint))

(deffunction is-of-type (?answer ?type ?valid)
  "Check that the answer has the right form"
  (if (eq ?type multi) then
    (foreach ?item ?valid
             (if (eq (sym-cat ?answer) (sym-cat ?item)) then
               (return TRUE)))
    (return FALSE))
    
  ;; plain text
  (return (> (str-length ?answer) 0)))

(defrule ask::ask-question-by-id
  "Given the identifier of a question, ask it"
  (declare (auto-focus TRUE))
  (MAIN::question (ident ?id) (text ?text) (valid $?valid) (type ?type))
  (not (MAIN::answer (ident ?id)))
  (MAIN::ask ?id)
  =>
  (ask-user ?text ?type ?valid)
  ((engine) waitForActivations))

(defrule ask::collect-user-input
  "Check an answer returned from the GUI, and optionally return it"
  (declare (auto-focus TRUE))
  (MAIN::question (ident ?id) (text ?text) (type ?type) (valid $?valid))
  (not (MAIN::answer (ident ?id)))
  ?user <- (user-input ?input)
  ?ask <- (MAIN::ask ?id)
  =>
  (if (is-of-type ?input ?type ?valid) then
    (retract ?ask ?user)
    (assert (MAIN::answer (ident ?id) (text ?input)))
    (return)
    else
    (retract ?ask ?user)
    (assert (MAIN::ask ?id))))

; Main window
(defglobal ?*frame* = (new JFrame "Medical Diagnosis Expert System"))
(?*frame* setDefaultCloseOperation (get-member JFrame EXIT_ON_CLOSE))
(?*frame* setSize 500 250)
(?*frame* setVisible TRUE)

; Question field
(defglobal ?*qfield* = (new JTextArea 5 40))
(bind ?scroll (new JScrollPane ?*qfield*))
((?*frame* getContentPane) add ?scroll)
(?*qfield* setText "Please wait...")

; Answer area
(defglobal ?*apanel* = (new JPanel))
(defglobal ?*afield* = (new JTextField 40))
(defglobal ?*afield-ok* = (new JButton OK))

(defglobal ?*acombo* = (new JComboBox (create$ "yes" "no")))
(defglobal ?*acombo-ok* = (new JButton OK))

(?*apanel* add ?*afield*)
(?*apanel* add ?*afield-ok*)
((?*frame* getContentPane) add ?*apanel* (get-member BorderLayout SOUTH))
(?*frame* validate)
(?*frame* repaint)

(deffunction read-input (?EVENT)
  "An event handler for the user input field"
  (assert (ask::user-input (sym-cat (?*afield* getText)))))

(bind ?handler (new jess.awt.ActionListener read-input (engine)))
(?*afield* addActionListener ?handler)
(?*afield-ok* addActionListener ?handler)

(deffunction combo-input (?EVENT)
  "An event handler for the combo box"
  (assert (ask::user-input (sym-cat (?*acombo* getSelectedItem)))))

(bind ?handler (new jess.awt.ActionListener combo-input (engine)))
(?*acombo-ok* addActionListener ?handler)

(deffacts MAIN::question-data
  (question (ident fever) (type multi) (valid yes no)
            (text "Are you ready for your medical diagnosis?
Lets start with something simple!
Do you have a fever?"))
  (question (ident headache) (type multi) (valid yes no)
            (text "Do you have a headache?"))
  (question (ident bodypains) (type multi) (valid yes no)
            (text "Are you suffering from muscle and joint pains?"))
  (question (ident hepatitis-yellow) (type multi) (valid yes no)
            (text "Are your eyes and skin yellow?"))
  (question (ident menin-light) (type multi) (valid yes no)
            (text "Are you experiencing intolerance to bright light?"))
  (question (ident menin-neck) (type multi) (valid yes no)
            (text "Do you have a stiff neck?"))
  (question (ident cold-stuffy) (type multi) (valid yes no)
            (text "Do you have a stuffy nose and watery eyes?"))
  (question (ident sorefatigue) (type multi) (valid yes no)
            (text "Do you have a sore throat or fatigue?"))
  (question (ident mono-swollen) (type multi) (valid yes no)
            (text "Do you have swollen lymph nodes and tonsils?"))
  (question (ident tuber-cough) (type multi) (valid yes no)
            (text "Are you experiencing blood tinged cough?"))         
  (question (ident dia-vision) (type multi) (valid yes no)
            (text "Are you experiencing blurry vision?"))
  (question (ident dia-thirst) (type multi) (valid yes no)
            (text "Are you experiencing increased hunger and thirst?"))
  (question (ident appetiteloss) (type multi) (valid yes no)
            (text "Do you have decreased appetite?"))    
  (question (ident tuber-sweat) (type multi) (valid yes no)
            (text "Do you experience night sweats or chills?"))
  (question (ident mal-convul) (type multi) (valid yes no)
            (text "Are you experiencing convulsions or chills?"))
  (question (ident ebola-pain) (type multi) (valid yes no)
            (text "Do you experience pain in the abdomen or chest?"))                
  (question (ident ebola-dia) (type multi) (valid yes no)
            (text "Are you suffering from diarrhea or vomiting?"))
  (question (ident hyper-vertigo) (type multi) (valid yes no)
            (text "Do you experience lightheadedness or vertigo?"))
  (question (ident hyper-heart) (type multi) (valid yes no)
            (text "Do you have a high blood pressure and fast heart rate?"))
  (question (ident hypo-heart) (type multi) (valid yes no)
            (text "Do you have low blood pressure and irregular heartbeat?"))
  (ask fever))

  
(reset)
(run-until-halt)