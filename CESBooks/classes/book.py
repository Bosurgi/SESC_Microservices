from errors.errors import IncompleteBookError
from helpers import is_valid_isbn, is_valid_year, is_valid_num_copies


class Book:
    def __init__(self, *args):
        for arg in args:
            if arg == '':
                raise IncompleteBookError('All fields are required.')

        if len(args) == 1:
            self.isbn = is_valid_isbn(args[0]['isbn'])
            self.title = args[0]['title']
            self.author = args[0]['author']
            self.year = is_valid_year(args[0]['year'])
            self.copies = is_valid_num_copies(args[0]['copies'])
        else:
            self.isbn = is_valid_isbn(args[0])
            self.title = args[1]
            self.author = args[2]
            self.year = is_valid_year(args[3])
            self.copies = is_valid_num_copies(args[4])
