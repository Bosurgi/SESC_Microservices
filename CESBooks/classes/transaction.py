
class Transaction:
    def __init__(self, *args):
        if len(args) == 1:
            self.id = args[0]['id']
            self.student_id = args[0]['student_id']
            self.book_isbn = args[0]['book_isbn']
            self.date_borrowed = args[0]['date_borrowed']
            self.date_returned = args[0]['date_returned']
        else:
            self.id = args[0]
            self.student_id = args[1]
            self.book_isbn = args[2]
            self.date_borrowed = args[3]
            self.date_returned = args[4]

        self.details = {'id': self.id, 'student_id': self.student_id, 'book_isbn': self.book_isbn, 'date_borrowed': self.date_borrowed,
                        'date_returned': self.date_returned}
