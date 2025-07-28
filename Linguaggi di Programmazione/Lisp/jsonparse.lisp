;;Rezzani Tommaso 874903

;;jsonread
(defun jsonread (filename)
  (let    ((in (open filename)))
    (parse (clearlist (filetolist in)))))

;;jsonparse
(defun jsonparse (JSONString)
  (if (not (stringp JSONString))
      (error "L'argomento deve essere di tipo stringa")
    (parse (clearlist  (strtolist JSONString)))))

;;parse
(defun parse (charlist)
  (cond   ((balanced-brackets charlist 'curly)
           (cons 'jsonobj 
                 (parseobjects (but-last (cdr charlist)))))
          ((balanced-brackets charlist 'square)
           (cons 'jsonarray 
                 (parsearrays (but-last (cdr charlist)))))
          (T (error "Parentesi non bilanciate"))))

;;clearlist
(defun clearlist (charlist)
  (remove-if 'is-whitespace 
             (parsenumbers (parsestrings charlist))))

;;strtolist
(defun strtolist (str)
  (if (equal str "")
      nil
    (cons (char str 0) (strtolist (subseq str 1)))))

;;listtostr
(defun listtostr (lst)
  (if (null lst)
      ""
    (concatenate 'string (string (car lst)) (listtostr (cdr lst)))))

;;parsestrings
(defun parsestrings (charlist &optional (buffer nil) (cnt 0))
  (cond   ((null charlist) nil)
          ((equal (car charlist) #\")
           (parsestrings (cdr charlist) buffer (+ cnt 1)))
          ((equal (car charlist) #\\)
           (parsestrings (cddr charlist)
			 (append buffer (list (cadr charlist))) cnt))
          ((= cnt 0)
           (cons (car charlist)
                 (parsestrings (cdr charlist) buffer cnt)))
          ((= cnt 1)
           (parsestrings (cdr charlist)
			 (append buffer (list (car charlist))) cnt))
          ((= cnt 2)
           (cons (listtostr buffer) (parsestrings charlist nil 0)))))

;;parsenumbers
(defun parsenumbers (charlist &optional (buffer nil) (flag 0) (float 0))
  (cond   ((null charlist) 
           (cond   ((not (null buffer)) 
                    (cons (parse-integer (listtostr buffer)) nil))
                   (T nil)))
          ((and (null buffer) (equal (car charlist) #\-))
           (parsenumbers (cdr charlist)
			 (append buffer (list (car charlist))) 1 float))
          ((and (= flag 1) (equal (car charlist) #\-))
           (error "Errore di sintassi"))
          ((and (equal (car charlist) #\.) (= flag 1) (= float 0))
           (parsenumbers (cdr charlist)
			 (append buffer (list (car charlist))) flag 1))
          ((equal (car charlist) #\.)
           (error "Errore di sintassi"))
          ((is-number (car charlist))
           (parsenumbers (cdr charlist)
			 (append buffer (list (car charlist))) 1 float))
          (T (cond    ((and (= flag 1) (= float 0)) 
                       (cons (parse-integer (listtostr buffer))
                             (parsenumbers charlist nil 0 0)))
                      ((= flag 1)
                       (cons (parse-float (listtostr buffer))
                             (parsenumbers charlist nil 0 0)))
                      (T (cons (car charlist)
                               (parsenumbers (cdr charlist) buffer 0 0)))))))

;;parseobjects
(defun parseobjects (source &optional (buffer nil) (open 0) (close 0))
  (cond   ((and (null source) (null buffer)) nil)
          ((null source) (list (parsepairs buffer)))
          ((and (equal (car source) #\,) (equal open close))
           (cons (parsepairs buffer)
                 (parseobjects (cdr source) nil open close)))
          ((is-open-bracket (car source))
           (parseobjects (cdr source)
			 (append buffer (list(car source))) (+ open 1) close))
          ((is-closed-bracket (car source))
           (parseobjects (cdr source)
			 (append buffer (list(car source))) open (+ close 1)))
          (T (parseobjects (cdr source)
			   (append buffer (list(car source))) open close))))

;;parsepairs
(defun parsepairs (pairs)
  (cond   ((equal (car pairs) #\:) (error "Chiave mancante"))
          ((and (not (stringp (car pairs))) (equal (second pairs) #\:))
           (error "La chiave deve essere una stringa"))
          (T (list (car pairs) (parsevalues (cddr pairs))))))

;;parsearrays
(defun parsearrays (source &optional (buffer nil) (open 0) (close 0))
  (cond   ((and (null source) (null buffer)) nil)
          ((null source) (list (parsevalues buffer)))
          ((and (equal (car source) #\,) (equal open close))
           (cons (parsevalues buffer)
                 (parsearrays (cdr source) nil open close)))
          ((is-open-bracket (car source))
           (parsearrays (cdr source)
                        (append buffer (list(car source))) (+ open 1) close))
          ((is-closed-bracket (car source))
           (parsearrays (cdr source)
                        (append buffer (list(car source))) open (+ close 1)))
          (T (parsearrays (cdr source)
			  (append buffer (list(car source))) open close))))

;;parsevalues
(defun parsevalues (value)
  (cond   ((null value) (error "Valore mancante"))
          ((numberp (car value)) (car value))
          ((stringp (car value)) (car value))
          ((equal (car value) #\{) (parse value))
          ((equal (car value) #\[) (parse value))))

;;is-whitespace
(defun is-whitespace (x)
  (cond   ((equal x #\space) T)
          ((equal x #\linefeed) T)
          ((equal x #\tab) T)
          ((equal x #\return) T)
          ((equal x "") T)
          (T nil)))

;;is-number
(defun is-number (x)
  (cond   ((stringp x) nil)
          ((and (>= (char-code x) 48) (<= (char-code x) 57)) T)
          (T nil)))

;;but-last
(defun but-last (x)
  (if (equal (rest x) nil) nil
    (append (list (first x)) (but-last (rest x)))))

;;is-open-bracket
(defun is-open-bracket (x)
  (or (equal x #\{) (equal x #\[)))

;;is-open-bracket
(defun is-closed-bracket (x)
  (or (equal x #\}) (equal x #\])))

;;balanced-brackets
(defun balanced-brackets (charlist &optional (type 'curly))
  (cond   ((equal type 'curly)
           (and (equal (car charlist) #\{)
                (equal (car(last charlist)) #\})))
          ((equal type 'square)
           (and (equal (car charlist) #\[)
                (equal (car(last charlist)) #\])))
          (T (error "Tipo di parentesi non riconosciuto"))))

;;filetolist
(defun filetolist (fd)
  (let    ((line (ignore-errors(strtolist(read-line fd)))))
    (cond   ((null line) line)
            (T (append line (filetolist fd))))))

;;jsonaccess
(defun jsonaccess (object &rest keys)
  (let    ((flat (flatten keys)))
    (cond   ((null flat) object)
            ((stringp (car flat))
             (jsonaccess (objectaccess (cdr object) (car flat)) (cdr flat)))
            ((numberp (car flat))
             (jsonaccess (arrayaccess (cdr object) (car flat)) (cdr flat)))
            (T (error "Errore")))))

;;objectaccess
(defun objectaccess (object key)
  (cond   ((and (null object) (not (null key)))
           (error "Chiave assente nell'oggetto"))
          ((equal key (caar object))
           (cadar object))
          (T (objectaccess (cdr object) key))))

;;arrayaccess
(defun arrayaccess (array index)
  (cond   ((or (not (integerp index)) (< index 0)) 
           (error "L'indice deve essere un numero intero >= di zero"))
          ((null array) (error "Indice fuori dai limiti"))
          ((equal index 0) (car array))
          (T (arrayaccess (cdr array) (- index 1)))))

;;jsondump
(defun jsondump (JSON filename)
  (let    ((out (open filename :direction :output 
                      :if-exists :rename-and-delete
                      :if-does-not-exist :create)))
    (format out (jsonreverse JSON))
    (close out)))

;;jsonreverse
(defun jsonreverse (object)
  (cond   ((equal (car object) 'jsonobj)
           (listtostr (flatten (objectreverse (cdr object) nil))))
          ((equal (car object) 'jsonarray)
           (listtostr (flatten (arrayreverse (cdr object) nil))))
          (T (error "L'argomento non è un oggetto"))))

;;objectreverse
(defun objectreverse (object buffer)
  (cond   ((null buffer)
           (objectreverse object (append buffer '(#\{))))
          ((null object)
           (append (but-last buffer) '(#\})))
          (T (objectreverse (cdr object)
			    (append buffer (pairreverse (car object)))))))

;;arrayreverse
(defun arrayreverse (object buffer)
  (cond   ((null buffer)
           (arrayreverse object (append buffer '(#\[))))
          ((null object)
           (append (but-last buffer) '(#\])))
          (T (arrayreverse (cdr object)
			   (append buffer (valuesreverse (car object)))))))

;;pairreverse
(defun pairreverse (pair)
  (list (write-to-string (car pair)) #\: (valuereverse (cadr pair)) #\,))

;;valuesreverse
(defun valuesreverse (value)
  (list (valuereverse value) #\,))

;;valuereverse
(defun valuereverse (value)
  (cond   ((stringp value) (write-to-string value))
          ((numberp value) (write-to-string value))
          ((equal (car value) 'jsonobj)
           (objectreverse (cdr value) nil))
          ((equal (car value) 'jsonarray)
           (arrayreverse (cdr value) nil))
          ((null value) "null")))

;;flatten
(defun flatten (x)
  (cond   ((null x) x)
          ((atom x) (list x))
          (T (append (flatten (first x))
		     (flatten (rest x))))))