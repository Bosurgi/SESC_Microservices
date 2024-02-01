import unittest
from classes.book import Book
from errors.errors import IncompleteBookError

VALID = {'isbn': '9781231231231', 'title': 'Book Title', 'author': 'An Author', 'year': 2020, 'copies': 2}
INVALID = {'isbn': '9', 'title': 232, 'author': 100, 'year': 'TwentyTwenty', 'copies': -1}
EMPTY_STRING = ''


class TestBook(unittest.TestCase):
    def test_create_book_with_individual_valid_args_creates_Book(self):
        test_book = Book(VALID['isbn'], VALID['title'], VALID['author'], VALID['year'], VALID['copies'])
        self.assertEqual(test_book.isbn, VALID['isbn'])
        self.assertEqual(test_book.title, VALID['title'])
        self.assertEqual(test_book.author, VALID['author'])
        self.assertEqual(test_book.year, VALID['year'])
        self.assertEqual(test_book.copies, VALID['copies'])

    def test_create_book_with_dictionary_of_valid_args_creates_Book(self):
        test_book = Book(VALID)
        self.assertEqual(test_book.isbn, VALID['isbn'])
        self.assertEqual(test_book.title, VALID['title'])
        self.assertEqual(test_book.author, VALID['author'])
        self.assertEqual(test_book.year, VALID['year'])
        self.assertEqual(test_book.copies, VALID['copies'])

    def test_create_book_with_one_empty_arg_raises_IncompleteBookError(self):
        try:
            Book(VALID['isbn'], VALID['title'], EMPTY_STRING, VALID['year'], VALID['copies'])
        except IncompleteBookError as e:
            self.assertEqual(type(e), IncompleteBookError)
            self.assertEqual('All fields are required.', str(e))
        else:
            self.fail('IncompleteBookError not raised')

    def test_create_book_with_invalid_ISBN_raises_ValueError(self):
        try:
            Book(INVALID['isbn'], VALID['title'], VALID['author'], VALID['year'], VALID['copies'])
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('The ISBN must contain 13 digits.', str(e))
        else:
            self.fail('ValueError not raised')

    def test_create_book_with_invalid_year_raises_ValueError(self):
        try:
            Book(VALID['isbn'], VALID['title'], VALID['author'], INVALID['year'], VALID['copies'])
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('Invalid year.', str(e))
        else:
            self.fail('ValueError not raised')

    def test_create_book_with_invalid_copies_raises_ValueError(self):
        try:
            Book(VALID['isbn'], VALID['title'], VALID['author'], VALID['year'], INVALID['copies'])
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('Invalid number of copies.', str(e))
        else:
            self.fail('ValueError not raised')


if __name__ == '__main__':
    unittest.main()
